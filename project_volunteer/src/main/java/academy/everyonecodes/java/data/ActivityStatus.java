package academy.everyonecodes.java.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class ActivityStatus
{
    @Id
    private Long id;

    @OneToOne
    @MapsId
    private Activity activity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    public ActivityStatus()
    {
    }

    public ActivityStatus(Long id, Activity activity, Status status)
    {
        this.id = id;
        this.activity = activity;
        this.status = status;
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ActivityStatus that = (ActivityStatus) o;
        return Objects.equals(id, that.id) && Objects.equals(activity, that.activity) && status == that.status;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, activity, status);
    }
}
