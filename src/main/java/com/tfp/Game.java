package com.tfp;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Game {
    private final Character character1;
    private final Character character2;
    private final CityMap cityMap;

    public Game(Character character1, Character character2, CityMap cityMap) {
        this.character1 = character1;
        this.character2 = character2;
        this.cityMap = cityMap;
    }

    private Position bfs() {
        int minTime = Integer.MAX_VALUE;
        Position meetingPosition = null;

        // Coada pentru algoritmul BFS
        Queue<Position> queue = new LinkedList<>();
        // Matrice pentru a marca pozițiile vizitate
        boolean[][] visited = new boolean[cityMap.getNumRows()][cityMap.getNumCols()];

        // Adăugăm pozițiile inițiale ale personajelor în coadă și le marcam ca vizitate
        queue.offer(character1.getPosition());
        queue.offer(character2.getPosition());
        visited[character1.getPosition().getX()][character1.getPosition().getY()] = true;
        visited[character2.getPosition().getX()][character2.getPosition().getY()] = true;

        // Parcurgem harta folosind algoritmul BFS
        while (!queue.isEmpty()) {
            Position currentPosition = queue.poll();

            int currentX = currentPosition.getX();
            int currentY = currentPosition.getY();

            // Calculăm timpul necesar fiecărui personaj pentru a ajunge la poziția curentă
            int time1 = character1.calculateTimeToPosition(currentX, currentY);
            int time2 = character2.calculateTimeToPosition(currentX, currentY);

            // Verificăm dacă timpul este același pentru ambii și este mai mic sau egal cu timpul minim anterior
            if (time1 == time2 && time1 <= minTime) {
                minTime = time1;
                meetingPosition = currentPosition;
            }

            // Generăm pozițiile vecine și le adăugăm în coadă dacă sunt valide și nevizitate
            List<Position> neighbors = generateNeighbors(currentX, currentY);
            for (Position neighbor : neighbors) {
                int neighborX = neighbor.getX();
                int neighborY = neighbor.getY();
                if (cityMap.isValidPosition(neighborX, neighborY) && !visited[neighborX][neighborY]) {
                    queue.offer(neighbor);
                    visited[neighborX][neighborY] = true;
                }
            }
        }

        return meetingPosition;
    }

    private List<Position> generateNeighbors(int x, int y) {
        List<Position> neighbors = new ArrayList<>();
        neighbors.add(new Position(x - 1, y));
        neighbors.add(new Position(x + 1, y));
        neighbors.add(new Position(x, y - 1));
        neighbors.add(new Position(x, y + 1));
        return neighbors;
    }

    public void play() {
        Position meetingPosition = bfs();
        if (meetingPosition == null) {
            System.out.println("Nu s-a putut realiza întâlnirea între personaje.");
            return;
        }

        int minTime = character1.calculateTimeToPosition(meetingPosition.getX(), meetingPosition.getY());
        System.out.println("Pozitia initiala a lui " + character1.getName() + " este: (" + (character1.getPosition().getX() + 1) + ", " + (character1.getPosition().getY() + 1) + ")");
        System.out.println("Pozitia initiala a lui " + character2.getName() + " este: (" + (character2.getPosition().getX() + 1) + ", " + (character2.getPosition().getY() + 1) + ")");

        System.out.println("Timpul până la punctul de întâlnire este: " + minTime);
        System.out.println("Punctul de întâlnire este: (" + (meetingPosition.getX() + 1) + ", " + (meetingPosition.getY() + 1) + ")");

        System.out.println("Datele introduse in fisierul \"maze.out\" sunt: "+minTime+" "+(meetingPosition.getX() + 1) + " " + (meetingPosition.getY() + 1));
        try {
            // Scriem rezultatul în fișierul "maze.out"
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/files/maze.out"));
            writer.write(minTime + " " + (meetingPosition.getX() + 1) + " " + (meetingPosition.getY() + 1));
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            // Citim datele de intrare din fișierul "maze.in"
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/files/maze.in"));

            // Citim dimensiunile hărții
            String[] dimensions = reader.readLine().split(" ");
            int numRows = Integer.parseInt(dimensions[0]);
            int numCols = Integer.parseInt(dimensions[1]);

            // Inițializăm matricea pentru harta
            char[][] verona = new char[numRows][numCols];
            for (int i = 0; i < numRows; i++) {
                String line = reader.readLine();
                verona[i] = line.toCharArray();
            }

            reader.close();

            // Creăm obiectul CityMap pentru harta
            CityMap cityMap = new CityMap(verona);
            Character romeo = null;
            Character julieta = null;

            // Căutăm pozițiile lui Romeo și Julieta pe hartă
            for (int i = 0; i < cityMap.getNumRows(); i++) {
                for (int j = 0; j < cityMap.getNumCols(); j++) {
                    char cell = verona[i][j];
                    if (cell == 'R') {
                        romeo = new Character("Romeo", new Position(i, j));
                    } else if (cell == 'J') {
                        julieta = new Character("Julieta", new Position(i, j));
                    }
                }
            }
            if (romeo == null || julieta == null) {
                throw new IllegalArgumentException("Personajele Romeo și Julieta nu au fost găsite pe hartă.");
            }

            // Inițializăm obiectul Game cu personajele și harta
            Game game = new Game(romeo, julieta, cityMap);
            game.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}