package com.bindinglogo.iwan.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.bindinglogo.iwan.MyGdxGame;
import com.sun.java_cup.internal.runtime.virtual_parse_stack;



public  class AbstractScreen implements Screen {	
    protected  final MyGdxGame game;
    protected  final Stage stage;
    protected BitmapFont font;
    //用于生成自由字体，包括支持中文的显示
    protected FreeTypeFontGenerator fontGenerator;
    protected FreeTypeBitmapFontData fontData;
    
    protected  Image backGroundImage;
    
    protected SpriteBatch batch;
    protected TextureAtlas atlas;
    private Table table;
    private Skin skin;
    protected static final int VIEWPORT_WIDTH = 1200, VIEWPORT_HEIGHT = 720;
    //需要增加支持的字符集
    private static final String SPECIAL_CHARS="消灭小怪兽";
    
    protected int screenWidth;
    protected int screenHeight;
	protected OrthographicCamera camera;
	private float ASPECT_RATIO=(float)1200/720;
	
	private Rectangle viewport;
    
    public AbstractScreen(
            MyGdxGame game )
        {
            this.game = game;
            this.stage=new Stage(VIEWPORT_WIDTH,VIEWPORT_HEIGHT,true);
            float w=Gdx.graphics.getWidth();
            float h=Gdx.graphics.getHeight();
            camera=new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
            camera.setToOrtho(true, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        }
    /**
     * 
     * @return
     */
    public String getName()
    {
    	return getClass().getSimpleName();
    }
    /**
     * 
     * @return
     */
    protected FreeTypeFontGenerator getFontGenerator()
    {
    	if(null==fontGenerator)
    	{
    		fontGenerator=new FreeTypeFontGenerator(Gdx.files.internal("fonts/yahei.ttf"));
    	}
    	return fontGenerator ;    	
    }
    /**
     * 
     * @param size
     * @param specialChars
     * @return
     */
    protected FreeTypeBitmapFontData getFontData (int size,String specialChars)
    {
    	if(null==fontData)
    	{
    		fontData=getFontGenerator().generateData(size, FreeTypeFontGenerator.DEFAULT_CHARS+specialChars, false);
    	}
    	return fontData;
    }
    /**
     *  获取自定义字体
     * @param size
     * @return
     */
    public BitmapFont getFont(int size)
    {
    	if(null==font)
    	{
    		FreeTypeBitmapFontData fontData=getFontData(size, SPECIAL_CHARS);
    		font=new BitmapFont(fontData,fontData.getTextureRegion(),false);
    	}
    	return font;
    }
    /**
     * 
     * @return
     */
    public SpriteBatch getBatch ()
    {
    	if(null==batch)
    	{
    		batch=new SpriteBatch();
    	}
    	return batch;
    }
    /**
     * 获取用Texture Packer 打包生成的纹理图集
     */
    protected TextureAtlas getAtlas()
    {
    	if(null==atlas)
    	{
    		atlas=new TextureAtlas(Gdx.files.internal("image-atlas/images.atlas"));
    	}
    	return atlas;
    }
    /**
     * 
     * @return
     */
    protected Skin getSkin()
    {
    	if(null==skin)
    	{
    		FileHandle skinFileHandle=Gdx.files.internal("skins/uiskin.json");
    		skin=new Skin(skinFileHandle);
    	}
    	return skin;
    }
  /**
   *   
   * @return
   */
    protected Table getTable()
    {
    	if(null==table)
    	{
    		table=new Table(getSkin());
    		//table.setFillParent(true);
    		if(MyGdxGame.DEV_MOD)
    		{
    			table.debug();
    		}
    		stage.addActor(table);
    	}
    	
    	return table;
    }
  /**
   *   
   * @return
   */
    public Image getbackGroundImage()
    {
    	if(null==backGroundImage)
    	{
    		backGroundImage=new Image();
    	}
    	return backGroundImage;
    }
  /**
   * 
   */
	@Override
	public void render(float delta) {
        // update the actors
        stage.act( delta );

        // (2) draw the result

        // clear the screen with the given RGB color (black)
        Gdx.gl.glClearColor( 1.0f, 1.0f, 1.0f, 1f );
        Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
        camera.update();
        //_camera.apply(Gdx.gl10);
        /*
        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
        				  (int) viewport.width, (int) viewport.height);
        				  */
        // draw the actors
        stage.draw();

        // draw the table debug lines
        Table.drawDebug( stage );
		
	}


	@Override
	public void show() {
        Gdx.app.log( MyGdxGame.LOG, "Showing screen: " + getName() );

        // set the stage as the input processor
        Gdx.input.setInputProcessor( stage );	
        
	}

	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {

		
	}

	@Override
	public void resume() {

		
	}

	@Override
	public void dispose() {
	
	}
	@Override
	public void resize(int width, int height)
	{
		Gdx.app.log( MyGdxGame.LOG, "Resizing screen: " + getName() + " to: " + width + " x " + height );
		/*
		backGroundImage.setWidth(width);
		backGroundImage.setHeight(height);
		backGroundImage.invalidateHierarchy();		
		*/
		
		// calculate new viewport
        float aspectRatio = (float)width/(float)height;
        float scale = 1f;
        Vector2 crop = new Vector2(0f, 0f);
        
        if(aspectRatio > ASPECT_RATIO)
        {
            scale = (float)height / (float)VIEWPORT_HEIGHT;
            crop.x = (width - VIEWPORT_HEIGHT * scale) / 2.0f;
        }
        else if(aspectRatio < ASPECT_RATIO)
        {
            scale = (float)width / (float)VIEWPORT_WIDTH;
            crop.y = (height - VIEWPORT_WIDTH * scale) / 2.0f;
        }
        else
        {
            scale = (float)width/(float)VIEWPORT_WIDTH;
        }

        float w = (float)VIEWPORT_WIDTH * scale;
        float h = (float)VIEWPORT_HEIGHT * scale;
        viewport = new Rectangle(crop.x, crop.y, w, h);
	}
	
	public OrthographicCamera getCamera()
	{
		if(null==camera)
		{
            camera=new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
            camera.setToOrtho(true, VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
		}
		return camera;
	}

}

