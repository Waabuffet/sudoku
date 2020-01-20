class Diagonaler{
  
  void fillGrid(Cell[][] grid, int count){
    fillDiagonals(grid, count);
    boolean filled = recursiveBacktracker(grid, count);
  
    if(filled){
      print("grid is filled");
    }else{
      print("grid is not filled");
    }
  }
  
  void fillDiagonals(Cell[][] grid, int count){
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
  
  boolean recursiveBacktracker(Cell[][] grid, int count){
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
  
  boolean checkAll(int i, int j, int num){
    if(isInRow(i,num)){
      return true;
    }else if(isInColumn(j, num)){
      return true;
    }else if(isInSquare(i,j,num)){
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
  
}
