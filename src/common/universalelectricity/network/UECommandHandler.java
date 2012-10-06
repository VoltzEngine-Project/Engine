package universalelectricity.network;

import java.util.List;

import universalelectricity.UniversalElectricity;


import net.minecraft.src.CommandBase;
import net.minecraft.src.ICommand;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.WrongUsageException;

public class UECommandHandler extends CommandBase {

	@Override
	public int compareTo(Object arg0) {
        return this.getCommandName().compareTo(((ICommand)arg0).getCommandName());
	}

	@Override
	public String getCommandName() {
		return "universalelectricity";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "/" + this.getCommandName();
	}

	@Override public List getCommandAliases() { return null; }

	@Override
	public void processCommand(ICommandSender sender, String[] arguments) {
        
        if(arguments.length <= 0) {
        	commandVersion(sender, arguments);
        	return;
        } 
        
    	throw new WrongUsageException(this.getCommandUsage(sender));
	}

	private void commandVersion(ICommandSender sender, String[] arguments) {
    	sender.sendChatToPlayer(String.format("You are using Universal Electricity version: " + UniversalElectricity.VERSION));
	}

}