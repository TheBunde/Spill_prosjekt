class TerrainColor{
    private float upper;
    private float lower;
    private color terraincolor;
    
    public TerrainColor(color terraincolor){
        this.terraincolor = terraincolor;
    }
    
    public color getColor(){
        return this.terraincolor;
    }
}
