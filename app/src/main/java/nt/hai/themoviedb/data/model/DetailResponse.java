package nt.hai.themoviedb.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DetailResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("cast")
    @Expose
    private List<Cast> cast = new ArrayList<>();
    @SerializedName("backdrops")
    @Expose
    private List<Image> backdrops = new ArrayList<>();
    @SerializedName("posters")
    @Expose
    private List<Image> posters = new ArrayList<>();

    @SerializedName("results")
    @Expose
    private List<Video> videos = new ArrayList<>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The cast
     */
    public List<Cast> getCast() {
        return cast;
    }

    /**
     * @param cast The cast
     */
    public void setCast(List<Cast> cast) {
        this.cast = cast;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<Image> backdrops) {
        this.backdrops = backdrops;
    }

    public List<Image> getPosters() {
        return posters;
    }

    public void setPosters(List<Image> posters) {
        this.posters = posters;
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void setVideos(List<Video> videos) {
        this.videos = videos;
    }

    public static class Cast implements Parcelable {

        @SerializedName("cast_id")
        @Expose
        private Integer castId;
        @SerializedName("character")
        @Expose
        private String character;
        @SerializedName("credit_id")
        @Expose
        private String creditId;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("order")
        @Expose
        private Integer order;
        @SerializedName("profile_path")
        @Expose
        private String profilePath;

        /**
         * @return The castId
         */
        public Integer getCastId() {
            return castId;
        }

        /**
         * @param castId The cast_id
         */
        public void setCastId(Integer castId) {
            this.castId = castId;
        }

        /**
         * @return The character
         */
        public String getCharacter() {
            return character;
        }

        /**
         * @param character The character
         */
        public void setCharacter(String character) {
            this.character = character;
        }

        /**
         * @return The creditId
         */
        public String getCreditId() {
            return creditId;
        }

        /**
         * @param creditId The credit_id
         */
        public void setCreditId(String creditId) {
            this.creditId = creditId;
        }

        /**
         * @return The id
         */
        public Integer getId() {
            return id;
        }

        /**
         * @param id The id
         */
        public void setId(Integer id) {
            this.id = id;
        }

        /**
         * @return The name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name The name
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return The order
         */
        public Integer getOrder() {
            return order;
        }

        /**
         * @param order The order
         */
        public void setOrder(Integer order) {
            this.order = order;
        }

        /**
         * @return The profilePath
         */
        public String getProfilePath() {
            return profilePath;
        }

        /**
         * @param profilePath The profile_path
         */
        public void setProfilePath(String profilePath) {
            this.profilePath = profilePath;
        }


        protected Cast(Parcel in) {
            castId = in.readByte() == 0x00 ? null : in.readInt();
            character = in.readString();
            creditId = in.readString();
            id = in.readByte() == 0x00 ? null : in.readInt();
            name = in.readString();
            order = in.readByte() == 0x00 ? null : in.readInt();
            profilePath = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            if (castId == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(castId);
            }
            dest.writeString(character);
            dest.writeString(creditId);
            if (id == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(id);
            }
            dest.writeString(name);
            if (order == null) {
                dest.writeByte((byte) (0x00));
            } else {
                dest.writeByte((byte) (0x01));
                dest.writeInt(order);
            }
            dest.writeString(profilePath);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<DetailResponse.Cast> CREATOR = new Parcelable.Creator<DetailResponse.Cast>() {
            @Override
            public Cast createFromParcel(Parcel in) {
                return new DetailResponse.Cast(in);
            }

            @Override
            public Cast[] newArray(int size) {
                return new Cast[size];
            }
        };
    }
    public class Image {
        @SerializedName("file_path")
        @Expose
        private String filePath;

        /**
         *
         * @return
         * The filePath
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         *
         * @param filePath
         * The file_path
         */
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

    public class Video {
        @SerializedName("key")
        @Expose
        private String filePath;

        /**
         *
         * @return
         * The filePath
         */
        public String getFilePath() {
            return filePath;
        }

        /**
         *
         * @param filePath
         * The file_path
         */
        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }
    }

}