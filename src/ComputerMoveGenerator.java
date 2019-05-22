import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ComputerMoveGenerator {
	
	//FELICITY's DATA
	public static final int ONE_DEF = 1; //O
	public static final int TWO_BLOCK_OFF = 2; // |OO[]
	public static final int TWO_BLOCK_DEF = 2; // |XX[] or OXX[]
	public static final int TWO_OPEN_DEF = 3; // []OO[]
	public static final int TWO_BLOCK_CAP = 3;
	public static final int THREE_BLOCK_DEF = 4; // |OOO[] or XOOO[]
	public static final int TWO_OPEN_OFF = 5; // []XX[]
	public static final int THREE_BLOCK_OFF = 5; // |XXX[] or OXXX[]
	public static final int CHRIS_HACK = 5;
	public static final int THREE_OPEN_OFF = 6; // []XXX[]
	public static final int TWO_CAP_DEF = 7;
	public static final int TWO_CAP = 8; // XOO[]
	public static final int THREE_OPEN_DEF = 9; //[]OOO[] or []O[]OO[]
	public static final int FOUR_BLOCK_DEF = 10; //just check paper
	public static final int FOUR_OPEN_DEF = 11;
	public static final int FOUR_OFF = 12;
	public static boolean FIRST_MOVE = true;
	
	//ROCHE's DATA
	public static final int OFFENSE = 1;
    public static final int DEFENSE = -1;
    public static final int NOT_ON_BOARD = -100;
    public static final int ONE_IN_ROW_DEF = 10;
    public static final int TWO_IN_ROW_DEF = 20;
    public static final int TWO_IN_ROW_OPEN = 21;
    public static final int TWO_IN_ROW_CAP = 22;  // We will adjust
    public static final int THREE_IN_ROW_ENCLOSE = 30;
    public static final int THREE_1_1_1_EXTREME_DEF = 31;
    public static final int THREE_IN_ROW_OPEN_DEF = 32;
    public static final int THREE_1_2_EXTREME_DEF = 32;
    public static final int THREE_2_1_EXTREME_DEF = 33;
	
	//gameplay variables
	GameBoard myGame;
	int myStone;
	int whichPlayer;
	ArrayList<CMObject> moves = new ArrayList<CMObject>();

	//CONSTRUCTOR
	public ComputerMoveGenerator(GameBoard gb, int stoneColor, int wP) {
		myStone = stoneColor;
		myGame = gb;
		whichPlayer = wP;
		
		System.out.println("Computer is playing as player " + myStone);
	}
	
	//BEHAVIORS
	public int[] getFComputerMove() {
		
		//setup
		int[] newMove = new int[2];
		newMove[0] = -1;
		newMove[1] = -1;
		moves.clear();
		
		//generate / sort moves
		findMoves();
		sortPriorities();
		
		printPriorites();
		System.out.println();
		
		//run a move
		if(FIRST_MOVE == true) {
			if(checkFirstMove(newMove[0], newMove[1]) == true){
				CMObject ourMove = moves.get(0);
				newMove[0] = ourMove.getRow();
				newMove[1] = ourMove.getCol();
			} else {
				newMove = generateSafeMove();
			}
		} else {
			if (moves.size() > 0) {
				CMObject ourMove = moves.get(0);
				newMove[0] = ourMove.getRow();
				newMove[1] = ourMove.getCol();
			} else {
				newMove = generateRandomMove(); 
			}
		}
		
		
		//timer
		try {
			sleepForAMove();
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return newMove;
	}
	
	public int[] getRComputerMove() {
		//setup
		int[] newMove = new int[2];
		newMove[0] = -1;
		newMove[1] = -1;
		moves.clear();
				
		//generate / sort moves
		findRMoves();
		sortPriorities();
				
				printPriorites();
				System.out.println();
				
				//run a move
				if(FIRST_MOVE == true) {
					if(checkFirstMove(newMove[0], newMove[1]) == true){
						CMObject ourMove = moves.get(0);
						newMove[0] = ourMove.getRow();
						newMove[1] = ourMove.getCol();
					} else {
						newMove = generateSafeMove();
					}
				} else {
					if (moves.size() > 0) {
						CMObject ourMove = moves.get(0);
						newMove[0] = ourMove.getRow();
						newMove[1] = ourMove.getCol();
					} else {
						newMove = generateRandomMove(); 
					}
				}
				
				
				//timer
				try {
					sleepForAMove();
				} catch (Exception e){
					e.printStackTrace();
				}
				
				return newMove;
	}
	
	//MAIN METHODS
	public void sortPriorities() {
		Collections.sort(moves);
	}
	
	public void findMoves() {
		for(int row = 0; row < GameBoard.NUM_SQUARE_SIDE; row++) {
			for(int col = 0; col < GameBoard.NUM_SQUARE_SIDE; col++) {
					findFour(row, col);
					findThree(row, col);
					findTwo(row, col);
					findOne(row, col);
			}
		}
	}
	
	public void findRMoves() { //for Mr. Roche's Computer Generator
		for(int row = 0; row < GameBoard.NUM_SQUARE_SIDE; row++) {
			for(int col = 0; col < GameBoard.NUM_SQUARE_SIDE; col++) {
				findThreeDefNormal(row, col);
				findThreeDefExtreme1_2(row, col);
				findThreeDefExtreme2_1(row, col);
				findThreeDefExtreme1_1_1(row, col);
				findTwoDef(row, col);
			}
		}
	}
	
	//Mr. Roche's methods
	
	public void findThreeDefNormal(int r, int c) {
		 for(int rL = -1; rL <= 1; rL++) {
	            for(int uD = -1; uD <= 1; uD++) {
	                try {
	                    
	                    if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
	                        if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
	                            if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
	                        
	                                //if r-rl is the wall 
	                                if(isOnBoard(r-rL, c-uD) == false) {
	                                    setMove(
	                                            r + (rL* 3), 
	                                            c + (uD * 3),
	                                            THREE_IN_ROW_ENCLOSE );
	                                   
	                                } else if (
	                                        myGame.getBoard()[r - rL][c -uD].getState() == GameBoard.EMPTY  
	                                        ) {
	                                    setMove(
	                                            r + (rL* 3), 
	                                            c + (uD * 3),
	                                            this.THREE_IN_ROW_OPEN_DEF);
	                                    
	                                } else if (
	                                        myGame.getBoard()[r - rL][c -uD].getState() == myStone 
	                                        ){
	                                    setMove(
	                                            r + (rL* 3), 
	                                            c + (uD * 3),
	                                            this.THREE_IN_ROW_ENCLOSE);
	                                    
	                                }
	                            }
	                        }
	                    }
	                }
	                            
	                              
	                 catch (ArrayIndexOutOfBoundsException e) {
	                    System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
	                }
	                
	            }
	        }   
	    }
	
	public void findThreeDefExtreme1_2(int r, int c) {
        
        //We start here on Wed
          //This runs in the 8 directions (9)
          for(int rL = -1; rL <= 1; rL++) {
              for(int uD = -1; uD <= 1; uD++) {
                  try {
                      
                      if(myGame.getBoard()[r + rL][c + uD].getState() == GameBoard.EMPTY ) {
                          if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
                              if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone * -1 ) {
                          
                                  setMove(
                                          r + (rL), 
                                          c + (uD),
                                          this.THREE_1_2_EXTREME_DEF);
                                  
                              }
                          }
                      }
                  }
                              
                                
                   catch (ArrayIndexOutOfBoundsException e) {
                      System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                  }
                  
              }
          }    
      }
	
	public void findThreeDefExtreme2_1(int r, int c) {
        
        //We start here on Wed
          //This runs in the 8 directions (9)
          for(int rL = -1; rL <= 1; rL++) {
              for(int uD = -1; uD <= 1; uD++) {
                  try {
                      
                      if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                          if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == GameBoard.EMPTY  ) {
                              if(myGame.getBoard()[r + (rL *3)][c + (uD*3)].getState() == myStone * -1 ) {
                          
                                  setMove(
                                          r + (rL * 2), 
                                          c + (uD * 2),
                                          this.THREE_2_1_EXTREME_DEF);
                                  
                              }
                          }
                      }
                  }
                              
                                
                   catch (ArrayIndexOutOfBoundsException e) {
                      System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                  }
                  
              }
          }    
      }
	
	public void findThreeDefExtreme1_1_1(int r, int c) {
	    
	    //We start here on Wed
	      //This runs in the 8 directions (9)
	      for(int rL = -1; rL <= 1; rL++) {
	          for(int uD = -1; uD <= 1; uD++) {
	              try {
	                  
	                  if(myGame.getBoard()[r + rL][c + uD].getState() == GameBoard.EMPTY ) {
	                      if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == myStone * -1 ) {
	                          if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
	                              if(myGame.getBoard()[r + (rL *4)][c + (uD*4)].getState() == myStone * -1 ) {
	                      
	                                if((int)(Math.random()*100) < 50) {
	                                    setMove(
	                                            r + (rL), 
	                                            c + (uD),
	                                            this.THREE_1_1_1_EXTREME_DEF);
	                                } else {
	                                    setMove(
	                                            r + (rL*3), 
	                                            c + (uD*3),
	                                            this.THREE_1_1_1_EXTREME_DEF);
	                                    
	                                }
	                              
	                              
	                              }
	                          }
	                      }
	                  }
	              }
	                          
	                            
	               catch (ArrayIndexOutOfBoundsException e) {
	                  System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
	              }
	              
	          }
	      }    
	  }
	
	public void findTwoDef(int r, int c) {
        //We start here on Wed
        //This runs in the 8 directions (9)
        for(int rL = -1; rL <= 1; rL++) {
            for(int uD = -1; uD <= 1; uD++) {
                try {
                    
                    if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
                        if(myGame.getBoard()[r + (rL *2)][c + (uD*2)].getState() == GameBoard.EMPTY ) {
                    
                            //if r-rl is the wall 
                            if(isOnBoard(r-rL, c-uD) == false) {
                                setMove(
                                        r + (rL* 2), 
                                        c + (uD * 2),
                                        TWO_IN_ROW_DEF );
                               
                            } else if (
                                    myGame.getBoard()[r - rL][c -uD].getState() == GameBoard.EMPTY  
                                    ) {
                                setMove(
                                        r + (rL* 2), 
                                        c + (uD * 2),
                                        this.TWO_IN_ROW_OPEN );
                                
                            } else if (
                                    myGame.getBoard()[r - rL][c -uD].getState() == myStone 
                                    ){
                                setMove(
                                        r + (rL* 2), 
                                        c + (uD * 2),
                                        this.TWO_IN_ROW_CAP);
                                
                            }
                            
                            
                            
                           //if r-rl, c-uD is open 
                            
                            
                           //if r-rl, c-uD is us..
                            
                            
                         
                            
                            //if r-rl, c-uD is opponent (we don't care)
                            
                            
                            
                            
//                            CMObject newMove = new CMObject();
//                            newMove.setRow(r + (rL* 2));
//                            newMove.setCol(c + (uD * 2));
//                            newMove.setPriority(ONE_IN_ROW_DEF);
//                            newMove.setMoveType(DEFENSE);
//                            dMoves.add(newMove);
                        
                        }
                    }
                    
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
                }
                
            }
        }
    
    
    
}
	
	//My methods
	public void findOne(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
					if(myGame.getBoard()[r][c].getState() == myStone * -1) {
						if(myGame.getBoard()[r+rL][c+uD].getState() == GameBoard.EMPTY) {
							setMove(r + rL, c + uD, ONE_DEF);
						}
					}
				} catch(ArrayIndexOutOfBoundsException e) {
					System.out.println("Off the board in findOneDef at [" + r + "," + c + "]");
				}
			}
		}
	}
	
	public void findTwo(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
					//defense
					if(myGame.getBoard()[r][c].getState() == myStone * -1) {
						if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
							if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == GameBoard.EMPTY ) {
                        		
                        			if(isOnBoard(r-rL, c-uD) == false) {
                        				setMove(r + (rL*2), c + (uD*2), TWO_BLOCK_DEF);
                        			} else if(myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
                        				setMove(r + (rL*2), c + (uD*2), TWO_CAP);
                        			} else if (myGame.getBoard()[r - rL][c - uD].getState() == GameBoard.EMPTY) {
                        				setMove(r + (rL*2), c + (uD*2), TWO_OPEN_DEF);
                        			}
							}
                        }
                    }
					
					//prevent captures
					if(myGame.getBoard()[r][c].getState() == myStone * -1) {
						if(myGame.getBoard()[r + rL][c + uD].getState() == myStone) {
							if(myGame.getBoard()[r+ (rL*2)][c + (uD*2)].getState() == myStone) {
								if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY) {
									setMove(r + (rL*3), c + (uD*3), TWO_CAP_DEF);
								}
							}
						}
					}
					
					//offense
					if(myGame.getBoard()[r][c].getState() == myStone) {
						if(myGame.getBoard()[r + rL][c + uD].getState() == myStone) {
							if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == GameBoard.EMPTY ) {
                        		
                        			if(isOnBoard(r-rL, c-uD) == false) {
                        				setMove(r + (rL*2), c + (uD*2), TWO_BLOCK_OFF);
                        			} else if (myGame.getBoard()[r - rL][c - uD].getState() == GameBoard.EMPTY) {
                        				setMove(r + (rL*2), c + (uD*2), TWO_OPEN_OFF);
                        			}
							}
                        }
                    } 
                } catch (ArrayIndexOutOfBoundsException e) {

                }
			}
		}
	}
	
	public void findThree(int r, int c) {
		
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
				//defense
				if(myGame.getBoard()[r][c].getState() == myStone * -1) {
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1 ) {
						if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
							if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
                    
								if(isOnBoard(r-rL, c-uD) == false) {
									setMove(r + (rL*3), c + (uD*3), THREE_BLOCK_DEF);
								} else if(myGame.getBoard()[r - rL][c - uD].getState() == GameBoard.EMPTY) {
                            			setMove(r + (rL*3), c + (uD*3), THREE_OPEN_DEF);
								}
							}
						}
					}
				}
				
				//chris hack
				if(myGame.getBoard()[r][c].getState() == myStone * -1) {
					if(myGame.getBoard()[r + rL][c + uD].getState() == GameBoard.EMPTY) {
						if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
							if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
								if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone * -1) {
									if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
										setMove(r+rL, c+uD, CHRIS_HACK);
									}
								}
							}
						}
					}
				}
				
				//offense
				if(myGame.getBoard()[r][c].getState() == myStone) {
					if(myGame.getBoard()[r + rL][c + uD].getState() == myStone) {
						if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone) {
							if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY ) {
                    
								if(isOnBoard(r-rL, c-uD) == false) {
									setMove(r + (rL*3), c + (uD*3), THREE_BLOCK_OFF);
								} else if(myGame.getBoard()[r - rL][c - uD].getState() == GameBoard.EMPTY) {
                            			setMove(r + (rL*3), c + (uD*3), TWO_OPEN_OFF);
								} else if(myGame.getBoard()[r - rL][c - uD].getState() == myStone * -1) {
									setMove(r + (rL*3), c + (uD*3), TWO_OPEN_OFF);
								}
							}
						}
					}
				}
				
                } catch (ArrayIndexOutOfBoundsException e) {

                }
			}
		}
	}
	
	public void findFour(int r, int c) {
		for(int rL = -1; rL <= 1; rL++) {
			for(int uD = -1; uD <= 1; uD++) {
				try {
					//defense
					if(myGame.getBoard()[r][c].getState() == myStone * -1) {
						if(myGame.getBoard()[r + rL][c + uD].getState() == myStone * -1) {
							if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone * -1 ) {
								if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone * -1 ) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == GameBoard.EMPTY ) {
										
										if(isOnBoard(r-rL, c-uD) == false) {
											setMove(r + (rL*4), c + (uD*4), FOUR_BLOCK_DEF);
										} else if (myGame.getBoard()[r - rL][c - uD].getState() == myStone) {
											setMove(r + (rL*4), c + (uD*4), FOUR_BLOCK_DEF);
										} else if(myGame.getBoard()[r - rL][c - uD].getState() == GameBoard.EMPTY) {
											setMove(r + (rL*4), c + (uD*4), FOUR_OPEN_DEF);
										}
									}
								} else if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone * -1) {
										if(myGame.getBoard()[r + (rL*5)][c + (uD*5)].getState() == GameBoard.EMPTY) {
											
											setMove(r + (rL*3), c + (uD*3), FOUR_BLOCK_DEF);
										}
									}
								}
							} else if (myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == GameBoard.EMPTY ) {
								if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone * -1 ) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone * -1) {
										if(myGame.getBoard()[r + (rL*5)][c + (uD*5)].getState() == GameBoard.EMPTY) {
											
											setMove(r + (rL*2), c + (uD*2), FOUR_BLOCK_DEF);
										}
									}
								}
							}
						}
					}
					//offense
					if(myGame.getBoard()[r][c].getState() == myStone) {
						if(myGame.getBoard()[r + rL][c + uD].getState() == myStone) {
							if(myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == myStone) {
								if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == GameBoard.EMPTY ) {
										
										setMove(r + (rL*4), c + (uD*4), FOUR_OFF);
									}
								} else if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == GameBoard.EMPTY) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone) {
										if(myGame.getBoard()[r + (rL*5)][c + (uD*5)].getState() == GameBoard.EMPTY) {
											
											setMove(r + (rL*2), c + (uD*2), FOUR_OFF);
										}
									}
								}
							} else if (myGame.getBoard()[r + (rL*2)][c + (uD*2)].getState() == GameBoard.EMPTY ) {
								if(myGame.getBoard()[r + (rL*3)][c + (uD*3)].getState() == myStone) {
									if(myGame.getBoard()[r + (rL*4)][c + (uD*4)].getState() == myStone) {
										if(myGame.getBoard()[r + (rL*5)][c + (uD*5)].getState() == GameBoard.EMPTY) {
											
											setMove(r + (rL*2), c + (uD*2), FOUR_OFF);
										}
									}
								}
							}
						}
					}
					
				} catch (ArrayIndexOutOfBoundsException e) {

                }
			}
		}
	}
	
	public void setMove(int r, int c, int p) {
        CMObject newMove = new CMObject();
        newMove.setRow(r);
        newMove.setCol(c);
        newMove.setPriority(p);
        moves.add(newMove);
        
    }
	
	public void printPriorites() {
		for(CMObject m: moves) {
			System.out.println(m);
		}
	}
	
	//OTHER
	public int[] generateRandomMove() {
		 int[] move = new int[2];
		 boolean done = false;
		 int newR, newC;
		
		 do {
			 newR = (int)(Math.random() * GameBoard.NUM_SQUARE_SIDE);
			 newC = (int)(Math.random() * GameBoard.NUM_SQUARE_SIDE);
			 
			 if(myGame.getBoard()[newR][newC].getState() == GameBoard.EMPTY) {
				 done = true;
				 move[0] = newR;
				 move[1] = newC;
			 }
					
		 } while(!done);
		 
		 return move;
	}
	
	public int[] generateSafeMove() {
		int[] move = new int[2];
		int newR, newC;
		boolean done = false;
		
		newR = (int)(Math.random() * 7) + 7;
		newC = GameBoard.INNER_START - 1;
		
		move[0] = newR;
		move[1] = newC;
		
		return move;
	}
	
	public boolean isOnBoard(int r, int c) {     
        boolean isOn = false;     
        if(r >= 0 && r < GameBoard.NUM_SQUARE_SIDE) {
            if(c >= 0 && c < GameBoard.NUM_SQUARE_SIDE) {
                isOn = true;
            }
        }
        return isOn;
    }
	
	public boolean checkFirstMove(int r, int c) {
		boolean okay = false;
		
		if(r >= GameBoard.INNER_START && r <= GameBoard.INNER_END) {
			if(c >= GameBoard.INNER_START && c <= GameBoard.INNER_END) {
				okay = true;
			}	
		}
		
		return okay;
	}
	
	public void sleepForAMove() throws InterruptedException {
		Thread currThread = Thread.currentThread();
		currThread.sleep(GameBoard.SLEEP_TIME);
	}
}
