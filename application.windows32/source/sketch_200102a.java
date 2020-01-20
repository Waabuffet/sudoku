import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class sketch_200102a extends PApplet {

int count = 9;
float w;
int difficulty = 1;
boolean editMode = false;

Cell[][] grid;

Diagonaler diagonaler;
Filler filler;
Hider hider;

public void setup(){
  
  w  = width / count + 0.7f;
  
  generateGrid();
}

public void generateGrid(){
  grid = new Cell[count][count];
  
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      float x = j * w, y = i * w;
      grid[i][j] = new Cell(x,y,w,i,j); 
    }
  }
  diagonaler = new Diagonaler();
  diagonaler.fillGrid(grid, count);
  hider = new Hider(count, diagonaler, difficulty);
  hider.hideCellsRandomly(grid);
  //hider.hideCellsRandomSymetricY(grid);
}

public void draw(){
  background(255);
  
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      grid[i][j].show(); 
    }
  }
  
  strokeWeight(2);
  line(width / 3    , 0             , width / 3    , height)        ;
  line(2 * width / 3, 0             , 2 * width / 3, height)        ;
  line(0            , height / 3    , width        , height / 3)    ;
  line(0            , 2 * height / 3, width        , 2 * height / 3);
}

public void mousePressed(){
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      grid[i][j].unchange();
      grid[i][j].noError();
    }
  }
  unhighlight();
  unSame();
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      float x = j * w, y = i * w;
      //index = i * count + j;
      if(mouseX > x && mouseX < (x + w) && mouseY > y && mouseY < (y + w)){
        highlight(i, j);
        grid[i][j].change();
        int cellNum = grid[i][j].number;
        if(cellNum > 0){
          yellowSame(cellNum);
        }
        break;
      }
    }
  }
}

public void keyPressed(){
  int val = key - 48;
  if(val == 64){
    editMode = !editMode;
  }
  
  boolean selected = false;
  
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      if(grid[i][j].selected){
        if(val > 0 && val < 10){
          checkIfAvailable(i, j, val);
          grid[i][j].setNumber(val);
          if(!hasDuplicateInRow(i) && !hasDuplicateInColumn(j) && !hasDuplicateInSquare(i,j)){
            grid[i][j].noError();
          }
          checkGrid();
        }else if(key == BACKSPACE){
          grid[i][j].setNumber(0);
          grid[i][j].noError();
        }else if(val == 64){
          grid[i][j].changeEdit();
        }
        selected = true;
        break;
      }
    }
    if(selected){break;}
  }
  if(!selected){
    //set difficulty and regenerate grid
    if(val > 0 && val <= 4){
      difficulty = val;
      generateGrid();
    }
  }
}

public void checkGrid(){
  boolean hasEmpty = false;
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      if(grid[i][j].number == 0){
        hasEmpty = true;
        break;
      }
    }
    if(hasEmpty){
      break;
    }
  }
  
  if(!hasEmpty){
    boolean hasError = hasAnyDuplicates();
     
     if(!hasError){
       for(int i = 0; i < count; i++){
        for(int j = 0; j < count; j++){
          grid[i][j].hasWon();
        }
       }
     }
  }
  
}

public boolean checkAll(int i, int j, int num){
  if(isInRow(i,num)){
    //print("is in row");
    return true;
  }else if(isInColumn(j, num)){
    //print("is in col");
    return true;
  }else if(isInSquare(i,j,num)){
    //print("is in square");
    return true;
  }
  return false;
}

public boolean isInRow(int i, int num){
  for(int x = 0; x < count; x++){
    if(grid[i][x].number == num){
      return true;
    }
  }
  return false;
}

public boolean isInColumn(int j, int num){
  for(int x = 0; x < count; x++){
    if(grid[x][j].number == num){
      return true;
    }
  }
  return false;
}

public boolean isInSquare(int i, int j, int num){
  int startX = i - (i % 3);
  int startY = j - (j % 3);
  
  for(int x = startX; x < (startX + 3); x++){
    for(int y = startY; y < (startY + 3); y++){
      if(grid[x][y].number == num){
        return true;
      }
    }
  }
  return false;
}


public void checkIfAvailable(int i, int j, int num){
  if(isInRow(i,num)){
    print("in row");
    grid[i][j].hasError();
  }else if(isInColumn(j, num)){
    print("in column");
    grid[i][j].hasError();
  }else if(isInSquare(i,j,num)){
    print("in square");
    grid[i][j].hasError();
  }
}


public void highlightRow(int i){
  for(int x = 0; x < count; x++){
    grid[i][x].highlight();
  }
}

public void highlightColumn(int j){
  for(int x = 0; x < count; x ++){
    grid[x][j].highlight();
  }
}

public void yellowSame(int num){
  boolean hasAlreadySame = false;
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      if(grid[i][j].number == num){
        grid[i][j].same();
        if(hasAlreadySame){
          grid[i][j].hasError();
        }
        hasAlreadySame = true;
      }
    }
    hasAlreadySame = false;
  }
}

public void unSame(){
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      grid[i][j].unsame();
    }
  }
}

public void highlight(int i, int j){
  highlightRow(i);
  highlightColumn(j);
}

public void unhighlight(){
  for(int x = 0; x < count; x++){
    for(int y = 0; y < count; y++){
      grid[x][y].unhighlight();
    }
  }
}

public boolean hasAnyDuplicates(){
  for(int i = 0; i < count; i++){
    if(hasDuplicateInRow(i)){
      return true;
    }
    if(hasDuplicateInColumn(i)){
      return true;
    }
    if(i % 3 == 0){
      if(hasDuplicateInSquare(i,i)){
        return true;
      }
    }
  }
  return false;
}

public boolean hasDuplicateInRow(int i){
  ArrayList<Integer> nums = new ArrayList<Integer>();
  for(int x = 0; x < count; x++){
    int aNum = grid[i][x].number;
    if(nums.contains(aNum) && aNum != 0){
      return true;
    }
    nums.add(aNum);
  }
  return false;
}

public boolean hasDuplicateInColumn(int j){
  ArrayList<Integer> nums = new ArrayList<Integer>();
  for(int x = 0; x < count; x++){
    int aNum = grid[x][j].number;
    if(nums.contains(aNum) && aNum != 0){
      return true;
    }
    nums.add(aNum);
  }
  return false;
}

public boolean hasDuplicateInSquare(int i, int j){
  ArrayList<Integer> nums = new ArrayList<Integer>();
  int startX = i - (i % 3);
  int startY = j - (j % 3);
  
  for(int x = startX; x < (startX + 3); x++){
    for(int y = startY; y < (startY + 3); y++){
      int aNum = grid[x][y].number;
      if(nums.contains(aNum) && aNum != 0){
        return true;
      }
      nums.add(aNum);
    }
  }
  return false;
}
class Cell{
  float x, y;
  float w;
  int i, j;
  boolean selected = false, highlighted = false, hasSame = false, sorted = false;
  boolean error = false, hasWon = false;
  int number;
  ArrayList<Integer> possibilities;
  boolean underEdit;
  
  Cell(float x, float y, float w, int i, int j){
    this.x = x;
    this.y = y;
    this.w = w;
    this.i = i;
    this.j = j;
    possibilities = new ArrayList<Integer>();
  }
  
  public void show(){
    if(selected){
      fill(255,0,0, 100);
    }else if(hasSame){
      fill(255,255,0);
    }else if(highlighted){
      fill(255,0,0,10);
    }else if(sorted){
      fill(0,0,200, 30);
    }else{
      noFill();
    }
    if(hasWon){
      fill(0,255,0);
    }
    if(error){
      fill(255,0,0);
    }
    stroke(0);
    strokeWeight(1);
    rect(this.x, this.y, this.w, this.w);
    
    if(this.number > 0){
      fill(0);
      textSize(25);
      text(this.number, this.x + (this.w / 2) - 7, this.y + (this.w / 2) + 7);
    }
    
    if(this.possibilities.size() > 0){
      fill(0);
      textSize(12);
      String poss = String.valueOf(this.possibilities.get(0));
      for(int i = 1; i < this.possibilities.size(); i++){
        poss += " | " + String.valueOf(this.possibilities.get(i));
      }
      text(poss, this.x + 3, this.y + 15);
    }
  }
  
  public void addPossibility(int num){
    if(!this.possibilities.contains(num)){
      this.possibilities.add(num);
    }
  }
  
  public void addPosibilities(ArrayList<Integer> pos){
    if(pos.size() > 3){
      this.possibilities.clear();
      for(int i = 0; i < 3; i++){
        this.possibilities.add(pos.get(i));
      }
    }else{
      this.possibilities = pos;
    }
  }
  
  public ArrayList<Integer> getPossibilities(){
    return this.possibilities;
  }
  
  public void removePossibility(int num){
    if(this.possibilities.contains(num) && this.possibilities.size() > 1){
      this.possibilities.remove(num);
    }
    
    if(this.possibilities.size() == 1){
      this.setNumber(this.possibilities.get(0));
      this.possibilities.clear();
    }
  }
  
  public void hide(){
    this.number = 0;
  }
  
  public void hasError(){
    this.error = true;
  }
  
  public void hasWon(){
    this.hasWon = true;
    this.selected = false;
  }
  
  public void noError(){
    this.error = false;
  }
  
  public void isSorted(){
    this.sorted = true;
  }
  
  public void setNumber(int number){
    if(this.underEdit){
      if(this.possibilities.contains(number)){
        this.possibilities.remove(Integer.valueOf(number));
      }else if(number > 0){
        this.possibilities.add(number);
      }
      print(this.possibilities);
    }else{
      this.possibilities.clear();
      this.number = number;
    }
  }
  
  public void changeEdit(){
    this.underEdit = !this.underEdit;
    print(this.underEdit);
  }
  
  public boolean filled(){
    return this.number > 0;
  }
  
  public void change(){
    this.selected = true;
  }
  
  public void unchange(){
    this.selected = false;
  }
  
  public void highlight(){
    this.highlighted = true;
  }
  
  public void unhighlight(){
    this.highlighted = false;
  }
  
  public void same(){
    this.hasSame = true;
    if(this.highlighted && !this.selected){
      this.hasError();
    }
  }
  
  public void unsame(){
    this.hasSame = false;
  }
}
class Diagonaler{
  
  public void fillGrid(Cell[][] grid, int count){
    fillDiagonals(grid, count);
    boolean filled = recursiveBacktracker(grid, count);
  
    if(filled){
      print("grid is filled");
    }else{
      print("grid is not filled");
    }
  }
  
  public void fillDiagonals(Cell[][] grid, int count){
    for(int i = 0; i < count; i += 3){
      for(int x = i; x < (i + 3); x++){
        for(int y = i; y < (i + 3); y++){
          int rand;
          do {
            rand = floor(random(1, 10));
          } while(isInSquare(x, y, rand));
          
          grid[x][y].setNumber(rand);
        }
      }
    }
  }
  
  public boolean recursiveBacktracker(Cell[][] grid, int count){
    Cell cell = null;
    
    //assign next empty cell
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        Cell checkCell = grid[i][j];
        if(checkCell.number == 0){
          cell = checkCell;
        }
      }
    }
    
    if(cell == null){ //grid is filled
      return true;
    }
    
    print("cell " + cell.i + " " + cell.j + "\n");
    //sketchRef.draw();
    for(int i = 1; i <= count; i++){
      
      print("checking " + i + "\n");
      if(!checkAll(cell.i, cell.j, i)){
        print("found " + i + " compatible\n");
        cell.setNumber(i);
        if(recursiveBacktracker(grid, count)){
          return true;
        }else{
          cell.setNumber(0);
        }
      }
    }
    
    if(cell.number == 0){
      return false;
    }
    
    return true;
    
  }
  
  public boolean checkAll(int i, int j, int num){
    if(isInRow(i,num)){
      return true;
    }else if(isInColumn(j, num)){
      return true;
    }else if(isInSquare(i,j,num)){
      return true;
    }
    return false;
  }
  
  public boolean isInRow(int i, int num){
    for(int x = 0; x < count; x++){
      if(grid[i][x].number == num){
        return true;
      }
    }
    return false;
  }
  
  public boolean isInColumn(int j, int num){
    for(int x = 0; x < count; x++){
      if(grid[x][j].number == num){
        return true;
      }
    }
    return false;
  }
  
  public boolean isInSquare(int i, int j, int num){
    int startX = i - (i % 3);
    int startY = j - (j % 3);
    
    for(int x = startX; x < (startX + 3); x++){
      for(int y = startY; y < (startY + 3); y++){
        if(grid[x][y].number == num){
          return true;
        }
      }
    }
    return false;
  }
  
}
class Filler{
  
  int[][] easynums = {{0,0,7,0,5,0,0,2,3},
                    {0,6,0,0,0,0,0,9,7},
                    {0,0,2,7,4,0,8,1,0},
                    {0,0,4,9,0,0,0,8,1},
                    {7,0,0,1,0,4,0,0,2},
                    {9,8,0,0,0,2,6,0,0},
                    {0,7,9,0,3,5,1,0,0},
                    {2,4,0,0,0,0,0,6,0},
                    {8,1,0,0,9,0,2,0,0}};
                    
  int[][] meduiumnums = {
                        {0,0,8,0,6,0,0,0,2},
                        {7,0,2,0,8,0,3,0,5},
                        {1,0,3,0,0,0,0,0,0},
                        {3,0,1,0,0,8,0,0,0},
                        {9,0,0,1,0,6,0,0,8},
                        {0,0,0,5,0,0,4,0,1},
                        {0,0,0,0,0,0,9,0,6},
                        {8,0,6,0,2,0,5,0,4},
                        {4,0,0,0,7,0,8,0,0}};
                    
  int[][] hardnums = {
                      {2,0,3,0,9,0,0,6,0},
                      {0,0,0,7,2,0,4,0,0},
                      {0,0,1,0,0,6,0,0,0},
                      {0,5,0,0,0,0,6,0,8},
                      {6,0,0,0,1,0,0,0,2},
                      {7,0,9,0,0,0,0,3,0},
                      {0,0,0,8,0,0,1,0,0},
                      {0,0,2,0,6,5,0,0,0},
                      {0,8,0,0,7,0,3,0,4}};
                    
  int[][] hardnums2 = {
                      {0,0,2,0,0,0,1,8,3},
                      {0,0,0,2,0,0,0,4,0},
                      {0,5,0,0,0,8,0,0,7},
                      {2,4,3,0,0,0,0,1,0},
                      {0,0,0,0,9,0,0,0,0},
                      {0,9,0,0,0,0,6,5,2},
                      {4,0,0,8,0,0,0,3,0},
                      {0,8,0,0,0,6,0,0,0},
                      {7,3,6,0,0,0,8,0,0}};
                    
  int[][] evilnums = {
                      {0,0,0,4,6,0,0,7,0},
                      {0,0,0,0,0,2,9,0,3},
                      {0,0,0,0,0,0,0,4,5},
                      {4,8,0,0,9,0,0,0,0},
                      {2,0,0,1,0,8,0,0,7},
                      {0,0,0,0,2,0,0,8,1},
                      {8,2,0,0,0,0,0,0,0},
                      {1,0,5,7,0,0,0,0,0},
                      {0,3,0,0,4,9,0,0,0}};
  
  
  public void fillGrid(Cell[][] grid, int count){
    
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        grid[i][j].setNumber(evilnums[i][j]);
      }
    }
  }
  
  public void countEmpty(){
    int counter = 0;
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        if(easynums[i][j] == 0){
          counter++;
        }
      }
    }
    print("\ncount " + counter);
  }
}
class Hider{
  Cell[][] copyGrid, backupGrid;
  int count;
  Diagonaler diagonaler;
  int difficulty;
  
  final int[] DIFFICULTIES = {40, 45, 50, 55};
  
  Hider(int count, Diagonaler diagonaler, int difficulty){
    this.count = count;
    this.diagonaler = diagonaler;
    this.difficulty = difficulty;
    copyGrid = new Cell[count][count];
    backupGrid = new Cell[count][count];
    initializeGrids();
  }
  
  public void hideCellsRandomly(Cell[][] grid){
    //backup
    copyGrid(grid, backupGrid);
    
    do {
      //backup and restore backup
      copyGrid(backupGrid, grid);
      
      for(int i = 0; i < DIFFICULTIES[difficulty - 1]; i++){
        int randI = floor(random(0, count));
        int randJ = floor(random(0, count));
        while(grid[randI][randJ].number == 0){
          randI = floor(random(0, count));
          randJ = floor(random(0, count));
        }
        grid[randI][randJ].setNumber(0);
      }
      
      //copy to test on another grid
      copyGrid(grid, copyGrid);
    }while(!checkFeasibility());
    
    print("feasible");
  }
  
  public void hideCellsRandomSymetricY(Cell[][] grid){
    //backup
    copyGrid(grid, backupGrid);
    
    do {
      //backup and restore backup
      copyGrid(backupGrid, grid);
      
      for(int i = 0; i < DIFFICULTIES[difficulty - 1] / 2; i++){
        int randI = floor(random(0, count / 2));
        int randJ = floor(random(0, count / 2));
        while(grid[randI][randJ].number == 0){
          randI = floor(random(0, count / 2));
          randJ = floor(random(0, count / 2));
        }
        grid[randI][randJ].setNumber(0);
        grid[randI][randJ + (2 * (4 - randI))].setNumber(0);
      }
      copyGrid(grid, copyGrid);
    }while(!checkFeasibility());
    
    print("feasible");
  }
  
  public void initializeGrids(){
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        copyGrid[i][j] = new Cell(0,0,0,i,j);
      }
    }
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        backupGrid[i][j] = new Cell(0,0,0,i,j);
      }
    }
  }
  
  public void copyGrid(Cell[][] grid, Cell[][] grid2){
    for(int i = 0; i < count; i++){
      for(int j = 0; j < count; j++){
        grid2[i][j].setNumber(grid[i][j].number);
      }
    }
  }
  
  public boolean checkFeasibility(){
    return diagonaler.recursiveBacktracker(copyGrid, count);
  }
  
  
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "sketch_200102a" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
