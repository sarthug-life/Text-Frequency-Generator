import java.io.*;
import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.filechooser.*;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Q4 extends JFrame
{	public static String s="";
	public static String fin="";
	public static void main(String[] args)throws IOException
	{    
		//String fin="";
		//s="second";
		final JFrame f = new JFrame("Swing");
		f.setSize(400, 400);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JTextField stopWords = new JTextField(50);
		stopWords.setBounds(10, 10, 380, 30);
        

		JButton fileSelect=new JButton("Select File");
		fileSelect.setBounds(50,350,120, 40);

		JButton process=new JButton("Process");
		process.setBounds(250,350,120, 40);

		fileSelect.addActionListener(new ActionListener() {
         
            public void actionPerformed(ActionEvent e ){
            
            	JFileChooser fileChooser = new JFileChooser();
            	FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Documents And PDFs", "doc", "docx", "pdf");
                fileChooser.setFileFilter(filter);
            	int returnValue = fileChooser.showOpenDialog(null);
            	if (returnValue == JFileChooser.APPROVE_OPTION) {
            		File selectedFile = fileChooser.getSelectedFile();
            		//stopWords.setText(selectedFile.getAbsolutePath());
            		//System.out.println(selectedFile.getAbsolutePath());
            		s=selectedFile.getAbsolutePath();
            		
            		
               }
            }
         
        });

        process.addActionListener(new ActionListener(){
        
        	public void actionPerformed(ActionEvent evt) {
        	/*	Vector<String> header = new Vector<String>();
        		header.addElement("Words");
        		header.addElement("Frequency");
        		*/
        		String header[]={"word","frequency"};
        		try{
        		if(s.substring(s.length() -4).equals("docx")){
        			fin=docx(s);
        		}
        		else if(s.substring(s.length() -3).equals("pdf")){
        			fin=pdf(s);
        			
        		}
        		else if(s.substring(s.length() -3).equals("doc")){
        			fin=doc(s);
        		}
        		else {
        		fin=txt(s);
        			//System.out.println(fin);
        		}
        		  
        		}
        		catch (Exception exep)
        	    {
        	        exep.printStackTrace();
        	    }
        		
        		String list = stopWords.getText();
        		ArrayList slist = new ArrayList(Arrays.asList(list.split(",")));
        		//stopWords.setText(slist.get(2) + " done");

        		StringTokenizer hello = new StringTokenizer(fin.toLowerCase()," ,\t\n.!\"?;:=+(){}[]");  //chose which one u want  for delimimator
        		   
        	   //	ArrayList<String>stop =new ArrayList<String>(); 
        	   
        	
        	 	ArrayList<String>arr=new ArrayList<String>();
        		while (hello.hasMoreTokens()){	
        			String nw=hello.nextToken();
        			if(slist.contains(nw)){
        				continue;
        			}
        			arr.add(nw);
        		
        		}
        		
        		Collections.sort(arr);
        		Vector<String> word = new Vector<String>();
        		Vector<Integer> freq = new Vector<Integer>();
        		int count=1;
         		String z=arr.get(0);
         			for(String counter:arr){
         				if(counter.equals(z)){
         					count++;continue;
         				}
         				else{
         					word.addElement(z);
         					freq.addElement(count-1);
         			//System.out.println(z+" "+(count-1));
         		    z=counter;
         		    count=2;
         		}
         		}
         		//System.out.println(z+" "+(count-1));
         			word.addElement(z);
 					freq.addElement(count-1);
 			Object[][] tabledata=new Object[word.size()][2];		
 				for(int i=0;i<word.size();i++){
 					tabledata[i][0]=word.get(i);
 					tabledata[i][1]=freq.get(i);
 					//System.out.println(word.get(i)+" "+freq.get(i));
 				}
 				JTable table = new JTable(tabledata, header);
 				//table.setBounds(50,50,200,200);
 				JScrollPane sp=new JScrollPane(table);
 				sp.setBounds(10,50,380,280);
 				//table.setPreferredScrollableViewportSize(new Dimension(200,200));
 				f.add(sp);
 				f.setVisible(true);
 				
        	}
        	
        
        });
        
		
        
		f.add(fileSelect);
		f.add(process);
		f.add(stopWords);
		f.setLayout(null); 
		f.setVisible(true);
		//System.out.println(s);
		
	
}

	
	static String txt(String s)throws IOException{
		//System.out.println(s+" txt");
		File file = new File(s);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st;
	 	String finalstr="";
	 	  while ((st = br.readLine()) != null){
	 	  	StringTokenizer hello = new StringTokenizer(st.toLowerCase()," ,.!");  //chose which one u want  for delimimator
	 	   	 while (hello.hasMoreTokens()){	
	 			String nw=hello.nextToken();
	 			finalstr+=" "+nw;
	 		
	 		} 
	     }
		return finalstr;
	}
	static String pdf(String s)throws IOException{
		//System.out.println(s+" pdf");
	 	String finalstr="";
		File file = new File(s);
	    PDDocument document = PDDocument.load(file);
	     PDFTextStripper pdfStripper = new PDFTextStripper();
	     String text = pdfStripper.getText(document).toLowerCase();
	     StringTokenizer hello = new StringTokenizer(text.toLowerCase()," ,\n.!");
	 	 while (hello.hasMoreTokens()){	
				String nw=hello.nextToken();
				finalstr+=" "+nw;
			} 
	   //  System.out.println(text);
	     document.close();
	     return finalstr; 
	}
	static String docx(String s)throws IOException{
		//System.out.println(s+" docs");
	 	String finalstr="";
		 XWPFDocument docx = new XWPFDocument(new FileInputStream("../docxfile.docx"));
	     XWPFWordExtractor we = new XWPFWordExtractor(docx);
	     StringTokenizer hello = new StringTokenizer(we.getText().toLowerCase()," ,\n.!");
	     //System.out.println(we.getText());
	     while (hello.hasMoreTokens()){	
				String nw=hello.nextToken();
				finalstr+=" "+nw;
			} 
	     
	     return finalstr;
	}
	static String doc(String s){
		//System.out.println(s + "doc");
		String finalstr="";
		
		File file = null;
	    WordExtractor extractor = null;
	    try
	    {
	        file = new File(s);
	        FileInputStream fis = new FileInputStream(file.getAbsolutePath());
	        HWPFDocument document = new HWPFDocument(fis);
	        extractor = new WordExtractor(document);
	        String[] fileData = extractor.getParagraphText();
	        for (int i = 0; i < fileData.length; i++)
	        {
	            if (fileData[i] != null){
	            	finalstr+=" "+fileData[i].toLowerCase();
	            	/*
	            	StringTokenizer hello = new StringTokenizer(fileData[i].toLowerCase()," ,\n.!");
	            	  while (hello.hasMoreTokens()){	
	          			String nw=hello.nextToken();
	          			if(stop.contains(nw)){
	          				continue;
	          			}
	          			finalstr+=" "+nw;
	          		} */
	            	
	            }
	        }
	    }
	    catch (Exception exep)
	    {
	        exep.printStackTrace();
	    }
	    return finalstr;
	}
	
	}
