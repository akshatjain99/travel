package com.example.restservice;
import java.util.Arrays;

public class PlaceClass {

    private String name;
  private String[] categories;
  private String region;

  public String getName() {
    return this.name;
  }

  public String[] getCategories() {
    return this.categories;
  }

  public String getRegion() {
    return this.region;
  }

	public PlaceClass(String name, String[] categories, String region) {
        this.name = name;
        this.categories = categories;
        this.region = region;
    }

    @Override
    public String toString() {
    return "Value{" + "name=" + name +", categories=" + Arrays.toString(categories) +", region=" + region + '}';
  }
    
    
}
