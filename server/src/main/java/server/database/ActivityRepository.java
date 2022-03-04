package server.database;

import commons.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ActivityRepository extends JpaRepository<Activity,Long> {
    /**
     * Finds all activities which have a specific consumption
     * @param cons the consumption which we seek
     * @return a list of activities which has a specific consumption
     */
    @Query("SELECT a FROM Activity a WHERE a.CONSUMPTION=?1")
    public List<Activity> getByConsumption(int cons);
}
