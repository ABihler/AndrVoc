package de.albert.bihler.andrvoc;

import java.util.List;

import de.albert.bihler.andrvoc.model.Vokabel;

public class ApplicationSingleton
{

    private String test;
    private List<Vokabel> vocList;
    private static ApplicationSingleton appSingleton;

    public String getApplicationTest()
    {
        return test;
    }

    public void setApplicationTest(String test)
    {
        this.test = test;
    }

    public void setApplicationVocList(List<Vokabel> list) {
        vocList = list;
    }

    public List<Vokabel> getApplicationVocList() {
        return vocList;
    }

    private ApplicationSingleton()
    {
    }

    public static ApplicationSingleton getInstance()
    {
        if (appSingleton == null)
        {
            appSingleton = new ApplicationSingleton();
        }
        return appSingleton;
    }
}