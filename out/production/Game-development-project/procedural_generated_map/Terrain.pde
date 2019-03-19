import java.util.ArrayList;

abstract class Terrain{
    private ArrayList<TerrainColor> colors = new ArrayList<TerrainColor>();
    public Terrain(){
    
    }
    
    public color getColor(float terrainHeight){
        color c2 = this.colors.get(0).getColor();
        color c1 = this.colors.get(1).getColor();
        color newC = color(red(c1)+(red(c2)-red(c1))*terrainHeight, green(c1)+(green(c2)-green(c1))*terrainHeight, blue(c1)+(blue(c2)-blue(c1))*terrainHeight);
        //color newC = color(terrainHeight*255);
        return newC;
    }
    
    public boolean addTerrainColor(color terraincolor){
        this.colors.add(new TerrainColor(terraincolor));
        return true;
    }
}
