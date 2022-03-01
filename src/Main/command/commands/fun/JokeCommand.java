package Main.command.commands.fun;

import com.fasterxml.jackson.databind.JsonNode;

import Main.Yoruichi;
import Main.command.CommandContext;
import Main.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class JokeCommand implements ICommand {
	private Boolean state;
	
	@Override
	public void handle(CommandContext ctx) {
		final TextChannel channel = ctx.getChannel();
	
		if(!this.state) {
			ctx.getDisabled(channel);
			return;
		}
		
		WebUtils.ins.getJSONObject("https://apis.duncte123.me/joke").async((json) -> {
			if (!json.get("success").asBoolean()) {
				channel.sendMessage("Something went wrong, try again later").queue();
				System.out.println(json);
				return;
			}

			final JsonNode data = json.get("data");
			final String title = data.get("title").asText();
			final String url = data.get("url").asText();
			final String body = data.get("body").asText();

			final EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
					.setTitle(title, url)
					.setDescription(body);

			channel.sendMessageEmbeds(embed.build()).queue();
		});

	}

	@Override
	public String getName() {
		return "joke";
	}

	@Override
	public String getHelp() {
		return "Shows a random joke\n"
				+ "Usage: `"  + Yoruichi.prefix + "" + this.getName() + "`";
	}
	
	@Override
	public String getCategory() {
		return "Fun";
	}
	
	@Override
	public void setState(Boolean state) {
		this.state = state;
		
	}

	@Override
	public Boolean getState() {
		return this.state;
	}
	
	@Override
	public void showHelp(CommandContext ctx, TextChannel channel) {
		ctx.commandHelper(channel, this.getHelp() , this.getName().toUpperCase());
	}
}
