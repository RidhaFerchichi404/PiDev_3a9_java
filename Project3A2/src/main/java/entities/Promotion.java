package entities;

import java.math.BigDecimal;
import java.sql.Date;

public class Promotion {
    private int promotionId;
    private String codePromo;
    private String description;
    private String typeReduction;
    private BigDecimal valeurReduction;
    private Date dateDebut;
    private Date dateFin;
    private int abonnementId;
    private int salleId;

    public Promotion(String code, String description, String typeRemise, BigDecimal valeurRemise) {
        this.codePromo = codePromo;
        this.description = description;
        this.typeReduction = typeRemise;
        this.valeurReduction = valeurRemise;
    }

    public Promotion(int id, String description) {
        this.promotionId = id;
        this.description = description;
    }

    public Promotion(int id) {
        this.promotionId = id;
    }

    public Promotion() {

    }


    @Override
    public String toString() {
        return "Promotion{" +
                "promotionId=" + promotionId +
                ", codePromo='" + codePromo + '\'' +
                ", description='" + description + '\'' +
                ", typeReduction='" + typeReduction + '\'' +
                ", valeurReduction=" + valeurReduction +
                ", dateDebut='" + dateDebut + '\'' +
                ", dateFin='" + dateFin + '\'' +
                ", abonnementId=" + abonnementId +
                ", salleId=" + salleId +
                '}';
    }

    public int getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(int promotionId) {
        this.promotionId = promotionId;
    }

    public String getCodePromo() {
        return codePromo;
    }

    public void setCodePromo(String codePromo) {
        this.codePromo = codePromo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeReduction() {
        return typeReduction;
    }

    public void setTypeReduction(String typeReduction) {
        this.typeReduction = typeReduction;
    }

    public BigDecimal getValeurReduction() {
        return valeurReduction;
    }

    public void setValeurReduction(BigDecimal valeurReduction) {
        this.valeurReduction = valeurReduction;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = Date.valueOf(dateDebut);
    }

    public Date getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = Date.valueOf(dateFin);
    }

    public int getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(int abonnementId) {
        this.abonnementId = abonnementId;
    }

    public int getSalleId() {
        return salleId;
    }

    public void setSalleId(int salleId) {
        this.salleId = salleId;
    }
}
