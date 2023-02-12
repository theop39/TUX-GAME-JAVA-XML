package game;

public class Niveau {
    private int difficultee;
    private String mot;

    public Niveau() {}

    public int getDifficultee() {
        return difficultee;
    }

    public void setDifficultee(int difficultee) {
        this.difficultee = difficultee;
    }

    public String getMot() {
        return mot;
    }

    public void setMot(String mot) {
        this.mot = mot;
    }

    @Override
    public String toString() {
        return "Niveau [difficultee=" + difficultee + ", mot=" + mot + "]";
    }
}
