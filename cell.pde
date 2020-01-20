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
  
  void show(){
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
  
  void addPossibility(int num){
    if(!this.possibilities.contains(num)){
      this.possibilities.add(num);
    }
  }
  
  void addPosibilities(ArrayList<Integer> pos){
    if(pos.size() > 3){
      this.possibilities.clear();
      for(int i = 0; i < 3; i++){
        this.possibilities.add(pos.get(i));
      }
    }else{
      this.possibilities = pos;
    }
  }
  
  ArrayList<Integer> getPossibilities(){
    return this.possibilities;
  }
  
  void removePossibility(int num){
    if(this.possibilities.contains(num) && this.possibilities.size() > 1){
      this.possibilities.remove(num);
    }
    
    if(this.possibilities.size() == 1){
      this.setNumber(this.possibilities.get(0));
      this.possibilities.clear();
    }
  }
  
  void hide(){
    this.number = 0;
  }
  
  void hasError(){
    this.error = true;
  }
  
  void hasWon(){
    this.hasWon = true;
    this.selected = false;
  }
  
  void noError(){
    this.error = false;
  }
  
  void isSorted(){
    this.sorted = true;
  }
  
  void setNumber(int number){
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
  
  void changeEdit(){
    this.underEdit = !this.underEdit;
    print(this.underEdit);
  }
  
  boolean filled(){
    return this.number > 0;
  }
  
  void change(){
    this.selected = true;
  }
  
  void unchange(){
    this.selected = false;
  }
  
  void highlight(){
    this.highlighted = true;
  }
  
  void unhighlight(){
    this.highlighted = false;
  }
  
  void same(){
    this.hasSame = true;
    if(this.highlighted && !this.selected){
      this.hasError();
    }
  }
  
  void unsame(){
    this.hasSame = false;
  }
}
