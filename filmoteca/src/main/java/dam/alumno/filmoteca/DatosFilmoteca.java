package dam.alumno.filmoteca;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DatosFilmoteca
{
    private static DatosFilmoteca instancia = null;
    private static ObservableList<Pelicula> listaPeliculas = FXCollections.observableArrayList();
    private DatosFilmoteca()
    {
    }
    public static DatosFilmoteca  getInstancia()
    {
        if (instancia == null)
        {
            instancia = new DatosFilmoteca();
        }
        return instancia;
    }
    public static ObservableList<Pelicula> getListaPeliculas()
    {
        return listaPeliculas;
    }
    public static void setListaPeliculas(ObservableList<Pelicula> listaPeliculas)
    {
        DatosFilmoteca.listaPeliculas = listaPeliculas;
    }
}