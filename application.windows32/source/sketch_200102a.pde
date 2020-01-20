int count = 9;
float w;
int difficulty = 1;
boolean editMode = false;

Cell[][] grid;

Diagonaler diagonaler;
Filler filler;
Hider hider;

void setup(){
  size(600, 600);
  w  = width / count + 0.7;
  
  generateGrid();
}

void generateGrid(){
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

void draw(){
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

void mousePressed(){
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

void keyPressed(){
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

void checkGrid(){
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

boolean checkAll(int i, int j, int num){
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

boolean isInRow(int i, int num){
  for(int x = 0; x < count; x++){
    if(grid[i][x].number == num){
      return true;
    }
  }
  return false;
}

boolean isInColumn(int j, int num){
  for(int x = 0; x < count; x++){
    if(grid[x][j].number == num){
      return true;
    }
  }
  return false;
}

boolean isInSquare(int i, int j, int num){
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


void checkIfAvailable(int i, int j, int num){
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


void highlightRow(int i){
  for(int x = 0; x < count; x++){
    grid[i][x].highlight();
  }
}

void highlightColumn(int j){
  for(int x = 0; x < count; x ++){
    grid[x][j].highlight();
  }
}

void yellowSame(int num){
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

void unSame(){
  for(int i = 0; i < count; i++){
    for(int j = 0; j < count; j++){
      grid[i][j].unsame();
    }
  }
}

void highlight(int i, int j){
  highlightRow(i);
  highlightColumn(j);
}

void unhighlight(){
  for(int x = 0; x < count; x++){
    for(int y = 0; y < count; y++){
      grid[x][y].unhighlight();
    }
  }
}

boolean hasAnyDuplicates(){
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

boolean hasDuplicateInRow(int i){
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

boolean hasDuplicateInColumn(int j){
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

boolean hasDuplicateInSquare(int i, int j){
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
