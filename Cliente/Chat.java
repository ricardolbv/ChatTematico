import java.io.*;
import java.net.*;
import javax.swing.*;

public class Chat
{
    public static void main (String[] args)
    {
        if (args.length>2)
        {
            System.err.println ("Uso esperado: java Chat [HOST [PORTA]]\n");
            return;
        }

        Socket conexao=null;
        try
        {
            String host ="localhost";
            int    porta=12345;

            if (args.length>0)
                host = args[0];

            if (args.length==2)
                porta = Integer.parseInt(args[1]);

            conexao = new Socket (host, porta);
        }
        catch (Exception erro)
        {
            System.err.println ("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectOutputStream transmissor=null;
        try
        {
            transmissor =
            new ObjectOutputStream(
            conexao.getOutputStream());
        }
        catch (Exception erro)
        {
            System.err.println ("Indique o servidor e a porta corretos!\n");
            return;
        }

        ObjectInputStream receptor=null;
        try
        {
            receptor =
            new ObjectInputStream(
            conexao.getInputStream());
        }
        catch (Exception erro)
        {
            System.err.println ("Indique o servidor e a porta corretos!\n");
            return;
        }

        Parceiro servidor=null;
        try
        {
            servidor =
            new Parceiro (conexao, receptor, transmissor);
        }
        catch (Exception erro)
        {
            System.err.println ("Indique o servidor e a porta corretos!\n");
            return;
        }

        JanelaDeEscolhaDeNick janelaDeEscolhaDeNick=null;
        try
        {
            janelaDeEscolhaDeNick =
            new JanelaDeEscolhaDeNick (servidor);
        }
        catch (Exception erro)
        {} // try/catch anterior garante ausencia de erro aqui

        JanelaDeChat janelaDeChat =
        janelaDeEscolhaDeNick.getJanelaDeChat ();
        janelaDeEscolhaDeNick.setVisible      (false);
        janelaDeEscolhaDeNick.dispose         ();

        for(;;)
        {
            try
            {	
				Comunicado comunicado = servidor.envie ();
				
				/*
				//Esse if é a de tetnativa de comunicado
				if (comunicado.getComando().equals("disp"))
                {
					String test;
					test = comunicado.getComplemento1();
					System.out.println("Vamos ver oq tem: "+test);
				}*/
				if (comunicado==null) // servidor desconectou
					break;
				else if (comunicado.getComando().equals ("MSG"))
				{
					String remetente = comunicado.getComplemento1();
					String texto     = comunicado.getComplemento2();

    				janelaDeChat.novaMensagem (remetente,
											   "Você", // destinatario
											   texto);
				}
                else if (comunicado.getComando().equals ("ENT"))
                {
					String nick = comunicado.getComplemento1();
					janelaDeChat.novoUsuario   (nick);
					janelaDeChat.novaMensagem  (nick, "entrou");
				}
                else if (comunicado.getComando().equals ("FOI"))
                {
					String nick = comunicado.getComplemento1();
					janelaDeChat.removaUsuario (nick);
					janelaDeChat.novaMensagem  (nick, "saiu");
				}
                else if (comunicado.getComando().equals ("FIM"))
                {
					janelaDeChat.setVisible (false);

					JOptionPane.showConfirmDialog(null/*sem janela mãe*/,
					"Tente voltar a usar o serviço mais tarde!",
					"Bate-papo em manutenção",
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.INFORMATION_MESSAGE,
					null/*sem icone*/);

					servidor.adeus();
					System.exit(0);
				}
			}
			catch (Exception erro)
			{
				JOptionPane.showMessageDialog(null/*sem janela mãe*/,
				"Tente novamente mais tarde!",
				"Erro de conectividade",
				JOptionPane.ERROR_MESSAGE);

				System.exit(0);
			}
        }
    }
}
