package com.github.mclich.engmod.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

public class StaffParticleType extends ParticleType<StaffParticleData>
{
	private static final Codec<StaffParticleData> CODEC=RecordCodecBuilder.create(i->i.group(Codec.INT.fieldOf("color").forGetter(StaffParticleData::getColor)).apply(i, StaffParticleData::new));
	private static final @SuppressWarnings("deprecation") ParticleOptions.Deserializer<StaffParticleData> DESERIALIZER=new ParticleOptions.Deserializer<>()
	{
		public StaffParticleData fromCommand(ParticleType<StaffParticleData> type, StringReader reader) throws CommandSyntaxException
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
			return new StaffParticleData(((r&255)<<16)|((g&255)<<8)|(b&255));
		}

		public StaffParticleData fromNetwork(ParticleType<StaffParticleData> type, FriendlyByteBuf buffer)
		{
			return new StaffParticleData(buffer.readInt());
		}
	};
	
	public StaffParticleType()
	{
		super(false, StaffParticleType.DESERIALIZER);
	}

	@Override
	public Codec<StaffParticleData> codec()
	{
		return StaffParticleType.CODEC;
	}
}