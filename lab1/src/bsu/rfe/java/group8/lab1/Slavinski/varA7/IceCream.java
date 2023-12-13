package bsu.rfe.java.group8.lab1.Slavinski.varA7;

public class IceCream extends Food{

    private String sirup;
    public IceCream(String sirup) {
        super("Мороженко");
        this.sirup = sirup;
    }
    public String getSize() {
        return sirup;
    }
    public void setSize(String sirup) {
        this.sirup = sirup;
    }
    public String toString() {
        return super.toString() + " с сиропом '" + sirup + "'";
    }
    public void consume() {
        System.out.println(this + " съедено");
    }
}