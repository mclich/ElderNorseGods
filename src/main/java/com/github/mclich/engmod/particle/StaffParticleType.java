package com.github.mclich.engmod.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class StaffParticleType extends ParticleType<StaffParticleOptions>
{
	private static final Codec<StaffParticleOptions> CODEC=RecordCodecBuilder.create(i->i.group(Codec.INT.fieldOf("color").forGetter(StaffParticleOptions::getColor)).apply(i, StaffParticleOptions::new));
	private static final @SuppressWarnings("deprecation") ParticleOptions.Deserializer<StaffParticleOptions> DESERIALIZER=new ParticleOptions.Deserializer<>()
	{
		public StaffParticleOptions fromCommand(ParticleType<StaffParticleOptions> type, StringReader reader) throws CommandSyntaxException
		{
			reader.expect(' ');
			int r=reader.readInt();
			if(r>255||r<0) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, r);
			reader.expect(' ');
			int g=reader.readInt();
			if(g>255||g<0) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, g);
			reader.expect(' ');
			int b=reader.readInt();
			if(b>255||b<0) throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.readerInvalidInt().createWithContext(reader, b);
			return new StaffParticleOptions(((r&255)<<16)|((g&255)<<8)|(b&255));
		}

		public StaffParticleOptions fromNetwork(ParticleType<StaffParticleOptions> type, FriendlyByteBuf buffer)
		{
			return new StaffParticleOptions(buffer.readInt());
		}
	};
	
	public StaffParticleType()
	{
		super(false, StaffParticleType.DESERIALIZER);
	}

	@Override
	public Codec<StaffParticleOptions> codec()
	{
		return StaffParticleType.CODEC;
	}
}