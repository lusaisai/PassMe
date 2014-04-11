package PassMe;


public class App
{
    public static void main( String[] args )
    {
        if ( args.length < 1 ) {
            System.out.println("Usage: java -jar PassMe-1.0-SNAPSHOT.jar <passPhrase>");
            System.exit(1);
        } else {
            Cli cli = new Cli(args[0]);
            cli.waitForCommand();
        }
    }
}
