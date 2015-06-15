package c;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.Timer;

public class Snake extends Element implements ActionListener, Model{
	
	private int numberOfColumns;
	private int numberOfLines;
	private int snakeSpeed = 1;
	private int headLine,headColumn,tailLine,tailColumn;
	private final int type = 1;
	private boolean movement = false;
	private int direction = 2;
	private int lastMovement = 1;
	ArrayList <Vertebral> s = new ArrayList <Vertebral>(0);
	private Regular m;
	private Imag array[][];
	private Timer clock = new Timer(125/snakeSpeed, this );
	
	public Snake(int nl, int nc){
		numberOfColumns = nc;
		numberOfLines = nl;
		Imag array = new Imag(numberOfLines,numberOfColumns);
	}
	
	public void prepareTab(){
		for( int l = 0; l < numberOfLines; ++l )
			for( int c = 0; c < numberOfColumns; ++c )
				clearElement( l, c );	
		new Wall(numberOfLines,numberOfColumns,2);
//		Regular m = new Regular(1,4);
//		m.setElement();
		v.messageChange( "Clique em alguma tecla para iniciar o Jogo" );
	}
	
	public void setElement( int l, int c ,int type){
		v.elementChangeAt( l, c, type );
		
	}
	public void clearElement(int l, int c) {
		v.elementChangeAt( l, c, 0 );
	}
	
	
	public void start(){
		clock.start();
		Vertebral Head = new Vertebral (0,0,true,true);//snake head + tail inicialmente
		s.add(Head);
		setElement(Head.line , Head.column,type);
		v.messageChange( "A Jogar" );
		m = new Regular(4,4);
		v.elementChangeAt(m.line, m.column, m.getType());
	}
	
	public void addVertebral(){
		switch(lastMovement)
		{
			case 1:
				tailLine--;
				s.get(s.size()-1).tail=false;
				s.add(new Vertebral (tailLine,tailColumn,true));
				setElement(tailLine, tailColumn,2);
				break;
			case 2:
				tailLine++;
				s.get(s.size()-1).tail=false;
				s.add(new Vertebral (tailLine,tailColumn,true));
				setElement(tailLine, tailColumn,2);
				break;
			case 3:
				tailColumn--;
				s.get(s.size()-1).tail=false;
				s.add(new Vertebral (tailLine,tailColumn,true));
				setElement(tailLine, tailColumn,2);
				break;
			case 4:
				tailColumn++;
				s.get(s.size()-1).tail=false;
				s.add(new Vertebral (tailLine,tailColumn,true));
				setElement(tailLine, tailColumn,2);
				break;
			default: break;
			}
	}
	
	public void actionPerformed(ActionEvent e) {
		movement = true;
		clearElement(line,column);
		switch(direction)
		{
			case 1:
				if(s.size()>1){
					s.get(s.size()-2).tail=true;
					s.remove(s.size()-1);
				}
				clearElement( tailLine, tailColumn);// LIMPAR A TAIL
				s.get(0).line--;
				s.add(1, new Vertebral(headLine, headColumn, false));
				setElement( headLine, headColumn,2);//Colocar vertebra onde estava a Head
				headLine--;
				if( headLine == -1  ){
					headLine = numberOfLines -1;
					s.get(0).line=headLine;
				}
				s.get(0).line=headLine;
				setElement( headLine, headColumn,1);//Recolocar novamente a head
				tailLine=s.get(s.size()-1).line;
				tailColumn=s.get(s.size()-1).column;
				lastMovement = 2;
				break;
			case 2:
				if(s.size()>1){
					s.get(s.size()-2).tail=true;
					s.remove(s.size()-1);
				}
				clearElement( tailLine, tailColumn);// LIMPAR A TAIL
				s.get(0).line++;
				s.add(1, new Vertebral(headLine, headColumn, false));
				setElement( headLine, headColumn,2 );//Colocar vertebra onde estava a Head
				headLine++;
				if( headLine == numberOfLines  ) {
					headLine = 0;
					s.get(0).line=headLine;
				}
				
				setElement(headLine, headColumn,1);//Recolocar novamente a head
				tailLine=s.get(s.size()-1).line;
				tailColumn=s.get(s.size()-1).column;
				lastMovement = 1;
				break;
			case 3:
				if(s.size()>1){
					s.get(s.size()-2).tail=true;
					s.remove(s.size()-1);
				}
				clearElement( tailLine, tailColumn);// LIMPAR A TAIL
				s.get(0).column--;
				s.add(1, new Vertebral(headLine, headColumn, false));
				setElement( headLine, headColumn,2);//Colocar vertebra onde estava a Head
				headColumn--;
				if( headColumn == -1  ) {
					headColumn = numberOfColumns -1;
					s.get(0).column=headColumn;
				}
				setElement( headLine, headColumn,1);//Recolocar novamente a head
				tailLine=s.get(s.size()-1).line;
				tailColumn=s.get(s.size()-1).column;
				lastMovement = 4;
				break;
			case 4:
				if(s.size()>1){
					s.get(s.size()-2).tail=true;
					s.remove(s.size()-1);
				}
				clearElement( tailLine, tailColumn);// LIMPAR A TAIL
				s.get(0).column++;
				s.add(1, new Vertebral(headLine, headColumn, false));
				setElement( headLine, headColumn,2);//Colocar vertebra onde estava a Head
				headColumn++;
				if( headColumn == numberOfColumns  ) {
					headColumn = 0;
					s.get(0).column=headColumn;
				}
				setElement( headLine, headColumn,1);//Recolocar novamente a head
				tailLine=s.get(s.size()-1).line;
				tailColumn=s.get(s.size()-1).column;
				lastMovement = 3;
			default: break;
			}
		if((s.get(0).line == m.line) && (s.get(0).column == m.column)){
			addVertebral();
			m = new Regular((int)(Math.random()*numberOfLines),(int)(Math.random()*numberOfColumns));
			v.elementChangeAt(m.line, m.column, m.getType());
		}
		int l = s.get(0).line;
		int c = s.get(0).column;
		if(Imag.imag[l][c]==7)
			clock.stop();
	}

	public boolean getStatus() {
		return movement;
	}
	
	public void setDirection(int c){
		direction = c;
	}


	@Override
	public void stop() {
		if(movement)
			clock.stop();
		else
			clock.start();
	}

	public int getNumberOfLines() {return numberOfLines;}
	public int getNumberOfColumns() {return numberOfColumns;}
	public String getName() {return "Snake";}
	public void setViewer(Viewer v) {this.v = v;}	//LER
	
	class Wall{
		int numberOfLines;
		int numberOfColumns;
	
		
		public Wall(int l, int c, int t){//0=easiest----->3=hardest
			numberOfColumns = c;
			numberOfLines = l;
			
			switch(t)
			{
				case 0:
					lineX (20, 3, 32);
					lineX (21, 3, 32);
					break;
				case 1:
					lineX (20, 3, 32);
					lineX (21, 3, 32);
					lineY (16, 3, 10);
					lineY (16, 32, 38);
					break;
				case 2:
					lineX (20, 3, 32);
					lineX (21, 3, 32);
					lineX (10, 11, 21);
					lineX (11, 11, 21);
					lineX (30, 11, 21);
					lineX (31, 11, 21);
					lineY (16, 3, 10);
					lineY (16, 32, 38);
					lineY (8, 10, 31);
					lineY (7, 10, 31);
					lineY (24, 10, 31);
					lineY (25, 10, 31);
					break;
				case 3:
					lineX (20, 3, 32);
					lineX (21, 3, 32);
					lineX (10, 11, 21);
					lineX (11, 11, 21);
					lineX (30, 11, 21);
					lineX (31, 11, 21);
					lineY (16, 3, 10);
					lineY (16, 32, 38);
					lineY (8, 10, 31);
					lineY (7, 10, 31);
					lineY (24, 10, 31);
					lineY (25, 10, 31);	
					break;
				default: break;
				}
		}
	  private void lineY (int positionY, int initX, int endX){
		  for(int i=initX-1;i<endX;i++){
			  setElement(positionY-1,i,4);
			  Imag.setImage(positionY-1, i,7);;
		  }
	  } 
	  private void lineX (int positionX,int initY, int endY){
		  for(int i=initY-1;i<endY;i++){
			  setElement(i,positionX-1,4);
			  Imag.setImage(i, positionX-1,7);
		  }
	  }
	private void setElement(int i, int positionX, int j) {
		v.elementChangeAt(i, positionX, 4);
	}
	
	}
	
	
	static class Imag{
		public static int imag [][];
		private static int lines;
		private static int columns;
		public Imag(int l,int c){
			lines = l;
			columns = c;
			imag = new int[lines][columns];
			fillImag();
		}
		public static void setImage(int x, int y,int type){
			imag[x][y]=type;
		}
		public static void fillImageLine(int line,int b) {
				imag[line][b]=7;
		}
		public static void fillImageColumn(int column,int b){
				imag[b][column]=7;
		}
		
		public static void fillImag() {
			for(int i = 0;i<lines;i++)
				for(int j = 0;j<columns;j++)
					imag[i][j]=0;	
		}
		
	}


}
