package com.irpro;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SolutionParser {

	String url = "http://localhost:8983/solr";
	HttpSolrServer server = new HttpSolrServer(url);
	int docsAdded = 0;
	
	public static void main(String[] args){
		SolutionParser sp = new SolutionParser();
		sp.deleteAll();
		
		File file = new File("sb");
		sp.travelDir(file.getAbsolutePath());
		
		try{ 
			sp.server.commit(); 
			System.out.println("final commit done!!");
			System.out.print("total docs added: ");
			System.out.println(sp.docsAdded);}
		catch(Exception e){ System.out.println("error while commiting !!!");}
	}

	public void deleteAll(){
		try{
			server.deleteByQuery( "*:*" );
			System.out.println("deleted all docs");
		}catch(Exception e){
			System.out.println("!!! DeleteAll FAILED !!!");
		}

	}

	public void travelDir(String filePath){
		File file = new File(filePath);
		String[] filenames = file.list();
		for(String filename : filenames){
			File f = new File(filePath+'/'+filename);
			if(f.isDirectory())
				travelDir( f.getAbsolutePath() );
			else if(!f.getName().equals("index.htm")
					&& !f.getName().equals("summary.htm")){
				String[] verse = parseHTMLfile(f);
				indexVerse(verse);
			}
		}
		
	}

	public String[] parseHTMLfile(File file){
		String[] verse = new String[2];
		Document doc;
		
		try{
			doc = Jsoup.parse(file, "UTF-8");
			Element head = doc.select("p.h").first();
			verse[0] = "SB " + head.text().split(" ")[4];
			
			Elements ids = doc.select("p.c");
			verse[1] = "";
			for (Element id : ids)
				verse[1] += id.text()+" \n";
	
		}catch(IOException e){
			System.out.println("error in parsing "+file.getAbsolutePath());
		}

		return verse;
	}
	
	public void indexVerse(String[] verse){
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", verse[0]);
		doc.addField("text", verse[1]);
		try{
			server.add(doc);
			++docsAdded;
			if(docsAdded%2500 == 0){
				server.commit();
				System.out.print("commit\t");
				System.out.println(docsAdded);
			}
			
		}catch(Exception e){
			System.out.println("!!! DeleteAll FAILED !!!");
		}
	}

	public void executor(){
		Document doc;


		try{
			File input = new File("webone.htm");
			doc = Jsoup.parse(input, "UTF-8", "");
			Elements ids = doc.select("p.c");

			for (Element id : ids){

				System.out.println("\n"+id.text());
			}
		}catch(IOException e){
			System.out.println("error");
		}

	}
}

