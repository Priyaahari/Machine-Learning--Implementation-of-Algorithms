/*** Author :Vibhav Gogate
The University of Texas at Dallas
*****/


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import com.sun.prism.Image;
 
public class KMeans {
    public static void main(String [] args){
	if (args.length < 3){
	    System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
	    return;
	} 
	try{
	//	URL url = new URL(getCodeBase(), "examples/strawberry.jpg");
	    BufferedImage originalImage = ImageIO.read(new File(args[0]));
	    int k=Integer.parseInt(args[1]);
	    BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
	    
		ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 
	    //Interger comp_ratio = new File(args[2]).size();
		//System.out.println("Compression"+comp_ratio);
	    
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}	
    }
    
    private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
	int w=originalImage.getWidth();
	int h=originalImage.getHeight();
	BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
	Graphics2D g = kmeansImage.createGraphics();
	g.drawImage(originalImage, 0, 0, w,h , null);
	// Read rgb values from the image
	System.out.println("Reading the RGB values from the image...");
	int[] rgb=new int[w*h];
	int red[]=new int[w*h];
	int green[]=new int[w*h];
	int blue[]=new int[w*h];
	int count=0;
	// setting up min and max values from the rgb values in the image to set random num
	int min=0;
	int max=0;
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		rgb[count++]=kmeansImage.getRGB(i,j);
		if(count==1){
			min=kmeansImage.getRGB(i,j);
			max=kmeansImage.getRGB(i,j);
		}
		else
		{
			if(min>kmeansImage.getRGB(i,j))
			{
				min=kmeansImage.getRGB(i,j);
			}
			if(max<kmeansImage.getRGB(i,j))
			{
				max=kmeansImage.getRGB(i,j);
			}
		}
		
		
	    }
	}
	// Call kmeans algorithm: update the rgb values
	System.out.println("Calling KMeans...");
	kmeans(rgb,k,min,max);

	// Write the new rgb values to the image
	count=0;
	System.out.println("\nWriting back the values to image...");
	for(int i=0;i<w;i++){
	    for(int j=0;j<h;j++){
		kmeansImage.setRGB(i,j,rgb[count++]);
	    }
	}
	return kmeansImage;
    }

    
    
    
    // Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k,int min,int max){
    	
    	int red[]=new int[rgb.length];
    	int green[]=new int[rgb.length];
    	int blue[]=new int[rgb.length];
    	int karray[][]=new int[k][3];
    	boolean intilize_flag=true;
    	int random;
    	Random randomGenerator = new Random();
      
       for(int i=0;i<k;i++)
       {
    	   random=randomGenerator.nextInt((max - min) + 1) + min;
    	  
    	   //masking with repective values for red, blue and green
    	   karray[i][0]=(random >> 16) & 0x000000FF;
    	   karray[i][1]=(random >>8 ) & 0x000000FF;;
    	   karray[i][2]=(random) & 0x000000FF;;
    	   
		   //System.out.println("Cluster Number "+i+" R value "+karray[i][0]+" G value "+karray[i][1]+" B value "+karray[i][2]);
       }
       ArrayList<Integer>[] k_values =(ArrayList<Integer>[])new ArrayList[k];
       for(int i=0;i<k;i++)
       {
       k_values[i]=new ArrayList<Integer>();
       }
       for(int i=0;i<=25;i++)
       {
           if(i!=0)
    	   for(int z=0;z<k;z++)
           {
           k_values[z].clear();
           }
       	   
       assign_rgb_value(red,green,blue,rgb,karray,k_values,k,min,max);
       //System.out.println(k_values+" "+k_values[1]+" "+k_values[2]);
       new_means(red,green,blue,rgb,karray,k_values,k);
       }
       
       assign_values(red,green,blue,rgb,karray,k_values,k,min,max);
       }
    
      public static void assign_rgb_value(int red[],int green[],int blue[],int rgb[],int karray[][],ArrayList<Integer> k_values[],int k,int min,int max)
      {
    	
    	  //calculate to which cluster the value belongs to and label them to that respective cluster
    	  for(int j=0;j<rgb.length;j++)
      	{
              double minimum=0;
              int cluster=0;
              
      		red[j] = (rgb[j] >> 16) & 0x000000FF;
      		green[j] = (rgb[j] >>8 ) & 0x000000FF;
      		blue[j] = (rgb[j]) & 0x000000FF;
      		for(int i=0;i<k;i++){
      		double dist=Math.pow((karray[i][0]-red[j]),2)+Math.pow((karray[i][1]-green[j]),2)+Math.pow((karray[i][2]-blue[j]),2);
            if(i==0)
            {
            	minimum=dist;
      			cluster=i;
            }
            else
            {
            	if(dist<=minimum)
          		{
          			minimum=dist;
          			cluster=i;
          		}
            }

      		}
      		
      		k_values[cluster].add(j); 
      	}
      }
      
      
      public static void new_means(int red[],int green[],int blue[],int rgb[],int karray[][],ArrayList<Integer> k_values[],int k)
      {
    	int index;
      	int red_value=0;
      	int green_value=0;
      	int blue_value=0;
      	int total;
      	for(int z=0;z<k;z++){
	      	for(int i=0;i<k_values[z].size();i++)
	      	{
	      			//summing up the rgb values in each cluster
	      	         index=k_values[z].get(i);
	      	         red_value=red_value+red[index];
	      	         green_value=green_value+green[index];
	      	         blue_value=blue_value+blue[index];
	      	}
	      	if(k_values[z].size()==0)
	      	{
	            total=1;
	      	}
	      	else
	      	{
	      		total=k_values[z].size();
	      	}
	      	//calculate new mean ---sum of the cluster to the total points in the cluster
	      	karray[z][0]=red_value/total;
	      	karray[z][1]=green_value/total;
	      	karray[z][2]=blue_value/total;
	      	red_value=0;
	      	green_value=0;
	      	blue_value=0;
	      	
	      	}
      	
      }
      public static void assign_values(int red[],int green[],int blue[],int rgb[],int karray[][],ArrayList<Integer> k_values[],int k,int min,int max)
      {
    	int value;
    	int index;
    	int sum=0;
    	
    	for(int z=0;z<k;z++){
    		//create hash map
    		HashMap<Integer, Integer> hm = new HashMap();
    	if(k_values[z].size()==0)
      	{
    		//if there are no values for that crresponding color ,then set value as zero
            value=0;
      	}
      	else
      	{
      		//else store the index and values in the hash map
      		sum=0;
      	
      	for(int i=0;i<k_values[z].size();i++)
      	{
      		index=k_values[z].get(i);
      		try{
      		hm.put(rgb[index], hm.get(rgb[index])+1);
      		}
      		catch(Exception e)
      		{
      			hm.put(rgb[index], 1);
      		}
      		//sum up rgb[count] if value is present
 	        sum=rgb[index]+sum;
 	      
      	}
      	int value_max=0;
      	int index_max=0;
      	Iterator it = hm.entrySet().iterator();
      	Map.Entry pair = (Map.Entry)it.next();
      	value_max=(int)pair.getValue();
      	index_max=(int)pair.getKey();
      	while(it.hasNext())
      	{
      		//find the max value and its index by setting the iterator
      		Map.Entry pair1 = (Map.Entry)it.next();
      		if((int)pair1.getValue()>value_max){
      			
      			value_max=(int)pair1.getValue();
      			index_max=(int)pair1.getKey();
      			
      		}
      		
      	}
      	value=index_max;
      		
      	}
    	for(int i=0;i<k_values[z].size();i++)
      	{
    		//now store those values in rgb[] an return the final values 
      	         index=k_values[z].get(i);
      	         rgb[index]=value;
      	}
    	}
    	
      }
    }