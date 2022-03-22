package server.database;

import commons.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ScoreRepository extends JpaRepository<Score,Long> {
    
    @Query("SELECT s FROM Score s ORDER BY s.score DESC")
    public List<Score> getLeaderboard();
}
