package server.api;

import commons.Score;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import server.database.ScoreRepository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    private ScoreRepository repo;

    public ScoreController(ScoreRepository scoreRepository) {
        this.repo = scoreRepository;
    }

    @GetMapping(path = { "", "/" })
    public List<Score> getAll() {
        //return repo.findAll();
        return repo.getLeaderboard();
    }

    @PostMapping(path = { "", "/" })
    public void add(@RequestBody Score score) {
        repo.save(score);
    }

}
