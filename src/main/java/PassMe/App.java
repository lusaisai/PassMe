package PassMe;


public class App
{
    public static void main( String[] args )
    {
        if ( args.length < 1 ) {
            System.out.println("java App <passphrase>");
            System.exit(1);
        } else {
            Cli cli = new Cli(args[0]);
            cli.waitForCommand();
        }
    }
}
