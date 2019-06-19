import java.util.HashMap;
import bd.*;


public class Servidor
{
    public static void main (String[] args)
    {
        if (args.length>1)
        {
            System.err.println ("Uso esperado: java Servidor [PORTA]\n");
            return;
        }

        int porta=12345;

        if (args.length==1)
            porta = Integer.parseInt(args[0]);

        HashMap<String,Parceiro> usuarios =      
        new HashMap<String,Parceiro> ();
		
		
		//___________________________________________________________________________------->
		//Aqui vou incilaizar a conexao com o bd e tambem criar o hash map, por enquanto vou inicializar um!!
		HashMap<String,HashMap<String,Parceiro>> tematico = new HashMap<>();
		
		/*
		tematico.put("Dinheiro",null);
		tematico.put("Futebol",null);
		*/ //vou levar esse cara para a supervisora de conexao

        AceitadoraDeConexao aceitadoraDeConexao=null;
        try
        {
            aceitadoraDeConexao =
            new AceitadoraDeConexao  (porta, tematico);
            aceitadoraDeConexao.start();

        }
        catch (Exception erro)
        {
            System.err.println ("Escolha uma porta liberada para uso!\n");
            return;
        }
		
		
		//Vou pegar temas do BD
		
		//Vou enviar o comunicado dos tipos de temas
		
		
		

        for(;;)
        {
            System.out.println ("O servidor esta ativo! Para desativa-lo,");
            System.out.println ("use o comando \"desativar\"\n");
            System.out.print   ("> ");
			
			
            String comando=null;
            try
            {
                comando = Teclado.getUmString();
            }
            catch (Exception erro)
            {}
			

			// Preciso iterar sobre as paradas de conexao tematica
            if (comando.toLowerCase().equals("desativar")) // Talvez tenha que mexer aqui, para usar o hash String
            {
                synchronized (tematico)
                {
                    for (HashMap usuario:tematico.values())
                    {
						for(Object usuariosTema:usuario.values())
						{
							try
							{
								Parceiro users = (Parceiro)usuariosTema;
								users.receba (new Comunicado ("FIM"));
								users.adeus  ();
							}
							catch (Exception erro)
							{}
                    
						}
                    }
                }
				
	


                System.out.println ("O servidor foi desativado!\n");
                System.exit(0);
            }
            else
                System.err.println ("Comando invalido!\n");
        }
    }
}