package com.example.movieGame;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * tracks logic of gameplay
 *
 */
@Controller
public class ControllerClass {

    @GetMapping("/")
    public String initialDisplay() {
        return "ViewUI"; // Loads ViewUI.html from templates
    }

    @PostMapping("/setup")
    @ResponseBody
    public ResponseEntity<String> handleSetup(
            @RequestParam("player1") String player1,
            @RequestParam("player2") String player2,
            @RequestParam("winCondition") String winCondition
    ) {
        System.out.println("Player 1: " + player1);
        System.out.println("Player 2: " + player2);
        System.out.println("Win Condition: " + winCondition);

        //instantiate the game session
        GamePlay session = new GamePlay(player1, player2);

        //call/manage select win conditions
        //TODO - update this

        if (player1.isEmpty() || player2.isEmpty() || winCondition.isEmpty()) {
            return ResponseEntity.badRequest().body("Missing input");
        }

        return ResponseEntity.ok("Setup received");
    }
}