/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package webscraping;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
/**
 *
 * @author thiago
 */
public class WebScraping {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String url = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
        String downloadFolder = "pdf";
        // cria uma pasta pdf se ela nao exitir
        File folder = new File(downloadFolder);
        if(!folder.exists()){folder.mkdirs();}
        
        // conectar ao site e buscar os links dos pdfs
        try{ Document doc = Jsoup.connect(url).get();
        Elements links = doc.select("a[href$=.pdf]");
        List<String> pdfFiles = new ArrayList<>();
        
        // faz com que percorra os links encontados
        for(Element link :links){String pdfUrl= link.absUrl("href");
        
        String pdfLower= pdfUrl.toLowerCase();
       if (!pdfLower.matches(".*(anexo[\\s_-]?i).*") && !pdfLower.matches(".*(anexo[\\s_-]?ii).*")){continue;}
        
        //cria um nome de arquivo seguro
        // Criar um nome de arquivo seguro
        String rawFileName = pdfUrl.substring(pdfUrl.lastIndexOf("/") + 1);
        String safeFileName = rawFileName.replace(" ", "-")  
         .replaceAll("[^a-zA-Z0-9._-]", ""); 
        String fileName = downloadFolder + "/" + safeFileName; 

        downloadFile(pdfUrl, fileName);
        pdfFiles.add(fileName);}
        // para baixar os pdfs
            System.out.println("os arquivos pdf foram baixados ");
            
            if(!pdfFiles.isEmpty()){zipFiles(pdfFiles, downloadFolder + "/anexo.zip");
                System.out.println("Os arquivos foram compactados com sucesso");
            }else{System.out.println("Nenhum anexo 1 e 2 foram encontrado para Download");}
            
            
        }
        catch (Exception e){ e.printStackTrace();}
        }
        // TODO code application logic here

    private static void downloadFile(String pdfUrl, String fileName) {
        
        try(InputStream in = new URL(pdfUrl).openStream();
            FileOutputStream out = new FileOutputStream(fileName)){
            
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead= in.read(buffer))!= -1){ out.write(buffer,0,bytesRead);}
            System.out.println("download concluido " + fileName);} catch(IOException e){
                System.out.println("erro ao baixar o arquivo " + e.getMessage() );}
      
           }    

    private static void zipFiles(List<String> pdfFiles, String zipFileName) {
       
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
           ZipOutputStream zos = new ZipOutputStream(fos)) {     
             for (String file : pdfFiles) {
            File pdfFile = new File(file);
            if (!pdfFile.exists()) continue;
             try (FileInputStream fis = new FileInputStream(pdfFile)) {
                ZipEntry zipEntry = new ZipEntry(pdfFile.getName());
                zos.putNextEntry(zipEntry);
                  byte[] buffer = new byte[1024];
                int len;
                while ((len = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, len);
            }
                zos.closeEntry();
            }
        }

        
    } catch (IOException e) {       System.out.println("Erro ao compactar arquivos: " + e.getMessage());}
    
        }  
    }
 
                
    
    


            

