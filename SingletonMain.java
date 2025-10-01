public class SingletonMain {
    public static void main(String[] args){
        System.out.println("Singleton Pattern Demo");
        LoggerSingleton l1 = LoggerSingleton.getInstance();
        LoggerSingleton l2 = LoggerSingleton.getInstance();
        System.out.println("Same instance? " + (l1==l2));
        l1.log("This is a log entry.");
    }
}