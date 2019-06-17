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

        AceitadoraDeConexao aceitadoraDeConexao=null;
        try
        {
            aceitadoraDeConexao =
            new AceitadoraDeConexao  (porta, usuarios);
            aceitadoraDeConexao.start();

        }
        catch (Exception erro)
        {
            System.err.println ("Escolha uma porta liberada para uso!\n");
            return;
        }
		
		
		//Vou pegar temas do BD
		
		

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
			
			
			/*
			//Esse é a tentativa de enviar um comunicado para os clientes
			if ((comando.toLowerCase().equals("disp")))
			{
				synchronized (usuarios)
                {
                    for (Parceiro usuario:usuarios.values())
                    {
                        try
                        {
                            usuario.receba (new Comunicado ("disp","test"));
                        }
                        catch (Exception erro)
                        {}
                    }
                }
			}*/
			

            if (comando.toLowerCase().equals("desativar"))
            {
                synchronized (usuarios)
                {
                    for (Parceiro usuario:usuarios.values())
                    {
                        try
                        {
                            usuario.receba (new Comunicado ("FIM"));
                            usuario.adeus  ();
                        }
                        catch (Exception erro)
                        {}
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