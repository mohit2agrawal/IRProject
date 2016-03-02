package com.irpro;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class SolutionParser {

	public static void main(String[] args){
		SolutionParser sp = new SolutionParser();
		sp.executor();
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

