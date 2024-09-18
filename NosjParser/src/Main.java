public class Main {

    public static void main(String[] args) {
        if (args.length != 1) NosjParser.exitProgram("Invalid number of arguments");
        else System.out.println(NosjParser.parseNosj(NosjParser.readNosjFile(args[0])));
    }
}