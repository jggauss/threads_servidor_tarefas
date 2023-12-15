package br.com.alura.servidor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

import javax.management.RuntimeErrorException;
import javax.naming.InterruptedNamingException;

public class ComandoC2AcessaBanco implements Callable<String> {

	private PrintStream saida;

	public ComandoC2AcessaBanco(PrintStream saida) {
		this.saida = saida;
	}

	@Override
	public String call() throws Exception {
		System.out.println("Servidor recebeu comando c2 - Banco");
		
		saida.println("Processando comando c2 - Banco");
		
		Thread.sleep(10000);
		
		int numero = new Random().nextInt(100) + 1;
		
		saida.println("Servidor finalizou o comando c2 - Banco");
		
		return Integer.toString(numero);
	}

}
