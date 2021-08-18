package academy.everyonecodes.java.data.DTOs;

import java.util.Objects;
import java.util.Set;

public class OrganizerViewForVolunteerActivityViewDTO {
        private String name;
        private Set<String> roles;
        private double rating;

        public OrganizerViewForVolunteerActivityViewDTO(String name, Set<String> roles, double rating) {
            this.name = name;
            this.roles = roles;
            this.rating = rating;
        }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
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
        OrganizerViewForVolunteerActivityViewDTO that = (OrganizerViewForVolunteerActivityViewDTO) o;
        return Double.compare(that.getRating(), getRating()) == 0 && Objects.equals(getName(), that.getName()) && Objects.equals(getRoles(), that.getRoles());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRoles(), getRating());
    }
}
