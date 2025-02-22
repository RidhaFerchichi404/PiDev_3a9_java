package entities;

public class Exercice {

    private int id;
    private String description;
    private String image;
    private long idUser;
    private int idEquipement;
    private String nomExercice;

    public Exercice(String description, String image, long idUser, int idEquipement, String nomExercice) {
        this.description = description;
        this.image = image;
        this.idUser = idUser;
        this.idEquipement = idEquipement;
        this.nomExercice = nomExercice;
    }

    public Exercice(int id, String description, String image, long idUser, int idEquipement, String nomExercice) {
        this.id = id;
        this.description = description;
        this.image = image;
        this.idUser = idUser;
        this.idEquipement = idEquipement;
        this.nomExercice = nomExercice;
    }

    public Exercice() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public int getIdEquipement() {
        return idEquipement;
    }

    public void setIdEquipement(int idEquipement) {
        this.idEquipement = idEquipement;
    }

    public String getNomExercice() {
        return nomExercice;
    }

    public void setNomExercice(String nomExercice) {
        this.nomExercice = nomExercice;
    }

    @Override
    public String toString() {
        return "Exercice{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", idUser=" + idUser +
                ", idEquipement=" + idEquipement +
                ", nomExercice='" + nomExercice + '\'' +
                '}';
    }
}
