import java.util.ArrayList;

class River{
    private PVector startPoint;
    private PVector endPoint;
    private PVector vec;
    private ArrayList<PVector> generatedPoints;
    public River(PVector startPoint, PVector endPoint){
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.generatedPoints = new ArrayList<PVector>();
        
        this.vec = new PVector(endPoint.x - startPoint.x, endPoint.y - startPoint.y);
        int n = 100;
        float offY = 100;
        for (int i = 0; i <= n; i++){
            PVector newPoint = this.startPoint.copy();
            PVector vecCopy = this.vec.copy();
            vecCopy.mult((float)i/n);
            newPoint.add(vecCopy);
            newPoint.add(0, (float)(noise(offY) - 0.5)*100);
            //System.out.println(vecCopy);
            offY += 0.04;
            this.generatedPoints.add(newPoint);
        }
        
    }
    
    public void draw(){
        noFill();
        
        beginShape();
        strokeWeight(55);
        stroke(color(188, 124, 84));
        for (int i = 0; i < this.generatedPoints.size(); i++){
            PVector vec = this.generatedPoints.get(i);
            vertex(vec.x, vec.y);       
        }
        endShape();
        
        beginShape();
        strokeWeight(35);
        stroke(color(21, 147, 255));
        for (int i = 0; i < this.generatedPoints.size(); i++){
            PVector vec = this.generatedPoints.get(i);
            vertex(vec.x, vec.y);       
        }
        endShape();
        
        beginShape();
        strokeWeight(20);
        stroke(color(21, 108, 255));
        for (int i = 0; i < this.generatedPoints.size(); i++){
            PVector vec = this.generatedPoints.get(i);
            vertex(vec.x, vec.y);       
        }
        endShape();
        
        //textSize(32);
        //text(this.generatedPoints.get(this.generatedPoints.size()-1) + " ", 0, height-60);
        //text(this.vec + " ", 0, height-20);
    }
}
