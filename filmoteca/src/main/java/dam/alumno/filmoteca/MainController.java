package dam.alumno.filmoteca;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class MainController
{
    public Text textoTitulo;
    public Text textoAño;
    public Text textoDescripcion;
    public Text textoValoracion;
    public ImageView textoPoster;
    public TableView<Pelicula> tableView;
    public TableColumn<Pelicula, Integer> columnaId;
    public TableColumn<Pelicula, String> columnaTitulo;
    public TableColumn<Pelicula, Integer> columnaAño;
    public TableColumn<Pelicula, Double> columnaValoracion;
    public TextField buscador;
    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> observableList;
    public void initialize()
    {
        observableList = datosFilmoteca.getListaPeliculas();
        columnaId.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("id"));
        columnaTitulo.setCellValueFactory(new PropertyValueFactory<Pelicula, String>("title"));
        columnaAño.setCellValueFactory(new PropertyValueFactory<Pelicula, Integer>("year"));
        columnaValoracion.setCellValueFactory(new PropertyValueFactory<Pelicula, Double>("rating"));
        tableView.setItems(observableList);
        tableView.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                textoTitulo.textProperty().bind(newValue.titleProperty());
                textoAño.textProperty().bind(newValue.yearProperty().asString());
                textoDescripcion.textProperty().bind(newValue.descriptionProperty());
                textoValoracion.textProperty().bind(newValue.ratingProperty().asString());
                textoPoster.setImage(new Image(newValue.getPoster()));
            }
            else
            {
                textoTitulo.textProperty().unbind();
                textoAño.textProperty().unbind();
                textoDescripcion.textProperty().unbind();
                textoValoracion.textProperty().unbind();
            }
        }));
    }
    public void handlerSubList(ActionEvent actionEvent)
    {
        FilteredList<Pelicula> sublist = datosFilmoteca.getListaPeliculas().filtered(p -> p.getTitle().contains(buscador.getText()));
        tableView.setItems(sublist);
    }
    public void handlerNormal(ActionEvent actionEvent)
    {
        System.out.println(datosFilmoteca.getListaPeliculas());
        tableView.setItems(observableList);
    }
    public void handlerNuevo(ActionEvent actionEvent)
    {
        Scene escena = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("PeliculaView.fxml"));
        try
        {
            escena = new Scene(fxmlLoader.load());
        }
        catch (IOException e)
        {
            System.out.println("ERROR al cargar la ventana de nueva película");
            e.printStackTrace();
        }
        PelículaController controlador = fxmlLoader.getController();
        Stage st = new Stage();
        st.setScene(escena);
        st.setTitle("Añadir una nueva película");
        st.showAndWait();
        if (controlador.isOKClicked())
        {
            Pelicula newPelicula = controlador.getPelicula();
            Pelicula p = Collections.max(observableList, Comparator.comparingInt(Pelicula::getId));
            newPelicula.setId(p.getId()+1);
            tableView.getItems().add(newPelicula);
        }
    }
    public void handlerlEditar(ActionEvent actionEvent)
    {
        int indice = tableView.getSelectionModel().getSelectedIndex();
        if (indice < 0)
        {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("ATENCIÓN");
            alerta.setHeaderText(" No se ha seleccionado ninguna película");
            alerta.setContentText(" Por favor seleccione una película de la tabla para poder editarla");
            alerta.showAndWait();
            return;
        }
        Scene escena = null;
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("PeliculaView.fxml"));
        try
        {
            escena = new Scene(fxmlLoader.load());
        }
        catch (IOException e)
        {
            System.out.println("ERROR al cargar la ventana de editar película");
            e.printStackTrace();
        }
        PelículaController controlador = fxmlLoader.getController();
        controlador.setPelicula(tableView.getSelectionModel().getSelectedItem());
        Stage st = new Stage();
        st.setScene(escena);
        st.setTitle("Editar película");
        st.showAndWait();
        if (controlador.isOKClicked())
        {
            Pelicula peliculaEditada = controlador.getPelicula();
            Pelicula peliculaActual = tableView.getSelectionModel().getSelectedItem();
            peliculaActual.setTitle(peliculaEditada.getTitle());
            peliculaActual.setYear(peliculaEditada.getYear());
            peliculaActual.setDescription(peliculaEditada.getDescription());
            peliculaActual.setRating(peliculaEditada.getRating());
            peliculaActual.setPoster(peliculaEditada.getPoster());
        }
    }
    public void handlerEliminar(ActionEvent actionEvent)
    {
        int indice = tableView.getSelectionModel().getSelectedIndex();
        if (indice >= 0)
        {
            tableView.getItems().remove(indice);
        }
        else
        {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("ADVERTENCIA");
            alerta.setHeaderText("No se ha seleccionado ninguna película");
            alerta.setContentText("Por favor seleccione una película de la tabla para eliminar");
            alerta.showAndWait();
        }
    }
    public void handlerSalir(ActionEvent actionEvent)
    {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmar cerrar aplicaclión");
        alerta.setHeaderText("¿Estás seguro que quieres cerrar la aplicaclión sin guardar los cambios?");
        alerta.setContentText("Elige una opción para continuar:");
        Optional<ButtonType> resultado = alerta.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK)
        {
            System.exit(0);
        }
    }
}