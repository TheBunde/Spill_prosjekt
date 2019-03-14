import java.util.ArrayList;

class World{
    private float offX = 100;
    private float offY = 100;
    private float zoom = 0.01;
    private ArrayList<Terrain> terrains = new ArrayList<Terrain>();
    private River river;
    
    public World(){
        this.terrains.add(new Forest());
        this.river = new River(new PVector(0, (int)(Math.floor(Math.random()*800))), new PVector(800, (int)Math.floor(Math.random()*800)));
    }
    
    public void draw(){
        clear();
        loadPixels();
        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                int index = x + y * width;
                //noiseDetail(26, 0.6);
                float terrainHeight = noise((float)x*zoom + offX, (float)y*zoom + offY);
                color c = this.getColorFromTerrain(terrainHeight);
                pixels[index] = c;
                
            }
        }
        updatePixels();
        this.river.draw();
        //textSize(32);
        //text(floor(mouseY*0.1) + "  " + mouseX*0.1, 0, height-40);
    }
    
    public color getColorFromTerrain(float terrainHeight){
        return terrains.get(0).getColor(terrainHeight);      
 
        
    }
    
    public void addOffset(float offX, float offY){
        this.offX += offX;
        this.offY += offY;

    }
}
