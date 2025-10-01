import java.util.*;
public class Main {
    static Scanner sc=new Scanner(System.in);
    static Map<Integer, Device> devices = new HashMap<>();
    public static void main(String[] args){
        devices.put(1,new Device(1,"light",false));
        devices.put(2,new Device(2,"thermostat",false));
        devices.put(3,new Device(3,"door",true));
        System.out.println("Smart Home (simple)"); while(true){
            System.out.println("1.TurnOn 2.TurnOff 3.Status 4.Exit"); String c=sc.nextLine().trim();
            if(c.equals("1")){ System.out.print("Device id: "); int id=Integer.parseInt(sc.nextLine().trim()); devices.get(id).status=true; System.out.println("Turned on"); }
            else if(c.equals("2")){ System.out.print("Device id: "); int id=Integer.parseInt(sc.nextLine().trim()); devices.get(id).status=false; System.out.println("Turned off"); }
            else if(c.equals("3")){ for(Device d: devices.values()) System.out.println(d); } else break;
        }
    }
    static class Device{ int id; String type; boolean status; Device(int id,String t,boolean s){this.id=id;this.type=t;this.status=s;} public String toString(){ return "Device "+id+" ("+type+") status="+status; } }
}
