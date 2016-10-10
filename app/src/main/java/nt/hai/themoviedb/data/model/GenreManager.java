package nt.hai.themoviedb.data.model;


import java.util.ArrayList;
import java.util.List;

public class GenreManager {

    private List<Genre> genres;

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public class Genre {
        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private Genre getGenre(int id) {
        for (Genre genre : genres) {
            if (genre.getId() == id)
                return genre;
        }
        return null;
    }

    public List<Genre> getGenreList(List<Integer> ids) {
        List<Genre> list = new ArrayList<>();
        for (Integer integer : ids) {
            Genre genre;
            if ((genre = getGenre(integer)) != null) {
                list.add(genre);
            }
        }
        return list;
    }
}

