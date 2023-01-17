package pro.sky.animalshelter4.info;

public class dogInfoTest implements InfoTest{

    @Override
    public String infoAboutShelter() {
        return InfoTest.super.infoAboutShelter();
    }

    @Override
    public String AboutDog() {
        return "dog";
    }
}
