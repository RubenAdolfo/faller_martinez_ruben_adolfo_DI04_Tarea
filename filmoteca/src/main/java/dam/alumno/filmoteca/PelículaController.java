package dam.alumno.filmoteca;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class PelículaController implements Initializable
{
    public TextField fieldTitulo;
    public TextField fieldAño;
    public TextField fieldDescripcion;
    public Slider fieldValoracion;
    public TextField fieldPoster;
    public ImageView Poster;
    private DatosFilmoteca datosFilmoteca = DatosFilmoteca.getInstancia();
    private ObservableList<Pelicula> listaPeliculas = datosFilmoteca.getListaPeliculas();
    private Pelicula newPelicula = new Pelicula();
    private boolean okClick = false;
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
    }
    public boolean isOKClicked ()
    {
        return okClick;
    }
    public Pelicula getPelicula ()
    {
        return newPelicula;
    }
    public void setPelicula(Pelicula pelicula)
    {
        newPelicula.setId(pelicula.getId());
        newPelicula.setTitle(pelicula.getTitle());
        newPelicula.setYear(pelicula.getYear());
        newPelicula.setDescription(pelicula.getDescription());
        newPelicula.setRating(pelicula.getRating());
        newPelicula.setPoster(pelicula.getPoster());
        fieldTitulo.textProperty().set(pelicula.getTitle());
        fieldAño.textProperty().set(String.valueOf(pelicula.getYear()));
        fieldDescripcion.textProperty().set(pelicula.getDescription());
        fieldValoracion.adjustValue(pelicula.getRating());
        fieldPoster.textProperty().set(pelicula.getPoster());
        try
        {
            Poster.setImage(new Image(pelicula.getPoster()));
        }
        catch(IllegalArgumentException e)
        {
        }
    }
    public void handlerAceptar(ActionEvent actionEvent)
    {
        try
        {
            newPelicula.setTitle(String.valueOf(fieldTitulo.textProperty().get()));
            newPelicula.setYear(Integer.parseInt(fieldAño.textProperty().get()));
            newPelicula.setDescription(String.valueOf(fieldDescripcion.textProperty().get()));
            newPelicula.setRating((float) fieldValoracion.getValue());
            newPelicula.setPoster(String.valueOf(fieldPoster.textProperty().get()));
            okClick = true;
            Stage st = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            st.close();
        }
        catch(Exception e)
        {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Error");
            alerta.setHeaderText("Algo a ocurrido...");
            alerta.setContentText("Asegúrate que el campo año solo contenga números.");
            alerta.showAndWait();
        }
    }
    public void handlerCancelar(ActionEvent actionEvent)
    {
        okClick = false;
        Stage st = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        st.close();
    }
    public void handlerImage(ActionEvent actionEvent)
    {
        try
        {
            Poster.setImage(new Image(fieldPoster.textProperty().get()));
        }
        catch(IllegalArgumentException e)
        {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Error");
            alerta.setHeaderText("No se ha podido encontrar la imagen");
            alerta.setContentText("Asegúrate de que la URL introducida sea correcta");
            alerta.showAndWait();        }
    }
}
