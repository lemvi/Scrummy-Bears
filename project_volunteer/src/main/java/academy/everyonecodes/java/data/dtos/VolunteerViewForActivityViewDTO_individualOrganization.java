package academy.everyonecodes.java.data.dtos;

import java.util.Objects;

public class VolunteerViewForActivityViewDTO_individualOrganization {
    private String name;
    private double rating;

    public VolunteerViewForActivityViewDTO_individualOrganization() {
    }

    public VolunteerViewForActivityViewDTO_individualOrganization(String name, double rating) {
        this.name = name;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VolunteerViewForActivityViewDTO_individualOrganization that = (VolunteerViewForActivityViewDTO_individualOrganization) o;
        return Double.compare(that.getRating(), getRating()) == 0 && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRating());
    }

    @Override
    public String toString() {
        return "VolunteerViewForActivityViewDTO_individualOrganization{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                '}';
    }
}
