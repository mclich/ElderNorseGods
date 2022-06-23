package com.github.mclich.engmod.mixin;

import com.github.mclich.engmod.register.ENGMobEffects;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.controls.KeyBindsList;
import net.minecraft.client.gui.screens.controls.KeyBindsList.KeyEntry;
import net.minecraft.client.gui.screens.controls.KeyBindsScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.Arrays;
import java.util.List;

public class MixinAnemiaHandler
{
    @Mixin(KeyboardHandler.class)
    private static class MixinKeyboardHandler
    {
        @Inject(method="keyPress", at=@At("HEAD"), cancellable=true)
        private void cancelKeyPress(long window, int keyCode, int scanCode, int action, int modifiers, CallbackInfo callback)
        {
            Minecraft mc=Minecraft.getInstance();
            boolean isKeyDropOrSwapOffhand=mc.options.keyDrop.getKey().getValue()==keyCode||mc.options.keySwapOffhand.getKey().getValue()==keyCode;
            boolean isPlayerNotAllowedToPressKey=mc.player!=null&&mc.player.hasEffect(ENGMobEffects.ANEMIA.get())&&mc.gameMode!=null&&mc.gameMode.getPlayerMode().isSurvival();
            if(isPlayerNotAllowedToPressKey&&isKeyDropOrSwapOffhand) callback.cancel();
        }
    }

    @Mixin(KeyBindsScreen.class)
    private static class MixinKeyBindsScreen
    {
        @Shadow private Button resetButton;

        @Inject(method="init", at=@At("TAIL"))
        private void renderTooltip(CallbackInfo callback)
        {
            Minecraft mc=Minecraft.getInstance();
            if(mc.player!=null&&mc.player.hasEffect(ENGMobEffects.ANEMIA.get())&&mc.gameMode!=null&&mc.gameMode.getPlayerMode().isSurvival())
            {
                Component keyDisabled=Component.translatable("message.engmod.key_disabled").withStyle(ChatFormatting.YELLOW);
                Component anemiaActive=Component.translatable("message.engmod.anemia_active", mc.player.getName()).withStyle(ChatFormatting.YELLOW);
                List<FormattedCharSequence> list=Arrays.asList(keyDisabled.getVisualOrderText(), anemiaActive.getVisualOrderText());
                Button.OnTooltip warn=(button, poseStack, mouseX, mouseY)->((KeyBindsScreen)(Object)this).renderTooltip(poseStack, list, mouseX, mouseY);
                ObfuscationReflectionHelper.setPrivateValue(Button.class, this.resetButton, warn, "onTooltip");
            }
        }

        @Redirect(method="render", at=@At(value="FIELD", target="Lnet/minecraft/client/gui/components/Button;active:Z", opcode=Opcodes.PUTFIELD))
        private void updateActivity(Button button, boolean value)
        {
            Minecraft mc=Minecraft.getInstance();
            button.active=mc.player!=null?(mc.gameMode!=null&&mc.gameMode.getPlayerMode().isSurvival()?value&&!mc.player.hasEffect(ENGMobEffects.ANEMIA.get()):value):value;
        }
    }

    @Mixin(AbstractSelectionList.class)
    private static abstract class MixinAbstractSelectionList<E extends AbstractSelectionList.Entry<E>>
    {
        @Shadow protected int x0;
        @Shadow protected int x1;
        @Shadow protected int y1;
        @Shadow protected int y0;
        @Shadow private E hovered;
        @Shadow protected int width;
        @Shadow protected int height;
        @Shadow private boolean renderHeader;
        @Shadow private boolean renderBackground;
        @Shadow private boolean renderTopAndBottom;

        @Shadow public abstract int getRowLeft();
        @Shadow public abstract int getMaxScroll();
        @Shadow protected abstract int getMaxPosition();
        @Shadow public abstract double getScrollAmount();
        @Shadow protected abstract int getScrollbarPosition();
        @Shadow protected abstract void renderBackground(PoseStack poseStack);
        @Shadow public abstract boolean isMouseOver(double mouseX, double mouseY);
        @Shadow protected abstract E getEntryAtPosition(double mouseX, double mouseY);
        @Shadow protected abstract void renderDecorations(PoseStack poseStack, int mouseX, int mouseY);
        @Shadow protected abstract void renderHeader(PoseStack poseStack, int mouseX, int mouseY, Tesselator tesselator);
        @Shadow protected abstract void renderList(PoseStack poseStack, int x, int y, int mouseX, int mouseY, float ticks);

        private void renderScrollBar(BufferBuilder bufferBuilder, Tesselator tesselator)
        {
            int k1=this.getMaxScroll();
            if(k1>0)
            {
                RenderSystem.disableTexture();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                int i=this.getScrollbarPosition(), j=i+6;
                int l1=(int)((float)((this.y1-this.y0)*(this.y1-this.y0))/(float)this.getMaxPosition());
                l1=Mth.clamp(l1, 32, this.y1-this.y0-8);
                int i2=(int)this.getScrollAmount()*(this.y1-this.y0-l1)/k1+this.y0;
                if(i2<this.y0) i2=this.y0;
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferBuilder.vertex((double)i, (double)this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)j, (double)this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)j, (double)this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)i, (double)this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)i, (double)(i2+l1), 0.0D).color(128, 128, 128, 255).endVertex();
                bufferBuilder.vertex((double)j, (double)(i2+l1), 0.0D).color(128, 128, 128, 255).endVertex();
                bufferBuilder.vertex((double)j, (double)i2, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferBuilder.vertex((double)i, (double)i2, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferBuilder.vertex((double)i, (double)(i2+l1-1), 0.0D).color(192, 192, 192, 255).endVertex();
                bufferBuilder.vertex((double)(j-1), (double)(i2+l1-1), 0.0D).color(192, 192, 192, 255).endVertex();
                bufferBuilder.vertex((double)(j-1), (double)i2, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferBuilder.vertex((double)i, (double)i2, 0.0D).color(192, 192, 192, 255).endVertex();
                tesselator.end();
            }
        }

        /**@author
          *@reason*/
        @Overwrite
        @SuppressWarnings("JavadocDeclaration")
        public void render(PoseStack poseStack, int mouseX, int mouseY, float ticks)
        {
            this.renderBackground(poseStack);
            boolean isKeyBindsList=(Object)this instanceof KeyBindsList;
            Tesselator tesselator=Tesselator.getInstance();
            BufferBuilder bufferBuilder=tesselator.getBuilder();
            RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
            this.hovered=this.isMouseOver((double)mouseX, (double)mouseY)?this.getEntryAtPosition((double)mouseX, (double)mouseY):null;
            if(this.renderBackground)
            {
                RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                bufferBuilder.vertex((double)this.x0, (double)this.y1, 0.0D).uv((float)this.x0/32.0F, (float)(this.y1+(int)this.getScrollAmount())/32.0F).color(32, 32, 32, 255).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)this.y1, 0.0D).uv((float)this.x1/32.0F, (float)(this.y1+(int)this.getScrollAmount())/32.0F).color(32, 32, 32, 255).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)this.y0, 0.0D).uv((float)this.x1/32.0F, (float)(this.y0+(int)this.getScrollAmount())/32.0F).color(32, 32, 32, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)this.y0, 0.0D).uv((float)this.x0/32.0F, (float)(this.y0+(int)this.getScrollAmount())/32.0F).color(32, 32, 32, 255).endVertex();
                tesselator.end();
            }
            int j1=this.getRowLeft();
            int k=this.y0+4-(int)this.getScrollAmount();
            if(this.renderHeader) this.renderHeader(poseStack, j1, k, tesselator);
            if(isKeyBindsList) this.renderScrollBar(bufferBuilder, tesselator);
            this.renderList(poseStack, j1, k, mouseX, mouseY, ticks);
            if(this.renderTopAndBottom)
            {
                RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
                RenderSystem.setShaderTexture(0, GuiComponent.BACKGROUND_LOCATION);
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(519);
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                bufferBuilder.vertex((double)this.x0, (double)this.y0, -100.0D).uv(0.0F, (float)this.y0/32.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)(this.x0+this.width), (double)this.y0, -100.0D).uv((float)this.width/32.0F, (float)this.y0/32.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)(this.x0+this.width), 0.0D, -100.0D).uv((float)this.width/32.0F, 0.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, 0.0D, -100.0D).uv(0.0F, 0.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)this.height, -100.0D).uv(0.0F, (float)this.height/32.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)(this.x0+this.width), (double)this.height, -100.0D).uv((float)this.width/32.0F, (float)this.height/32.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)(this.x0+this.width), (double)this.y1, -100.0D).uv((float)this.width/32.0F, (float)this.y1/32.0F).color(64, 64, 64, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)this.y1, -100.0D).uv(0.0F, (float)this.y1/32.0F).color(64, 64, 64, 255).endVertex();
                tesselator.end();
                RenderSystem.depthFunc(515);
                RenderSystem.disableDepthTest();
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
                RenderSystem.disableTexture();
                RenderSystem.setShader(GameRenderer::getPositionColorShader);
                bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
                bufferBuilder.vertex((double)this.x0, (double)(this.y0+4), 0.0D).color(0, 0, 0, 0).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)(this.y0+4), 0.0D).color(0, 0, 0, 0).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)this.y0, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)this.y1, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferBuilder.vertex((double)this.x1, (double)(this.y1-4), 0.0D).color(0, 0, 0, 0).endVertex();
                bufferBuilder.vertex((double)this.x0, (double)(this.y1-4), 0.0D).color(0, 0, 0, 0).endVertex();
                tesselator.end();
            }
            if(!isKeyBindsList) this.renderScrollBar(bufferBuilder, tesselator);
            this.renderDecorations(poseStack, mouseX, mouseY);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        }
    }

    @Mixin(KeyEntry.class)
    private static class MixinKeyEntry
    {
        @Final @Shadow private Button changeButton;
        @Final @Shadow private Button resetButton;
        @Final @Shadow private KeyMapping key;

        @Inject(method="<init>", at=@At("TAIL"))
        private void renderTooltip(KeyBindsList keys, KeyMapping key, Component component, CallbackInfo callback)
        {
            String keyName=key.getName();
            Minecraft mc=Minecraft.getInstance();
            if(mc.player!=null&&mc.player.hasEffect(ENGMobEffects.ANEMIA.get())&&mc.gameMode!=null&&mc.gameMode.getPlayerMode().isSurvival()&&(mc.options.keyDrop.getName().equals(keyName)||mc.options.keySwapOffhand.getName().equals(keyName)))
            {
                KeyBindsScreen screen=ObfuscationReflectionHelper.getPrivateValue(KeyBindsList.class, keys, "keyBindsScreen");
                Component keyDisabled=Component.translatable("message.engmod.key_disabled").withStyle(ChatFormatting.YELLOW);
                Component anemiaActive=Component.translatable("message.engmod.anemia_active", mc.player.getName()).withStyle(ChatFormatting.YELLOW);
                List<FormattedCharSequence> list=Arrays.asList(keyDisabled.getVisualOrderText(), anemiaActive.getVisualOrderText());
                Button.OnTooltip warn=(button, poseStack, mouseX, mouseY)->screen.renderTooltip(poseStack, list, mouseX, mouseY);
                ObfuscationReflectionHelper.setPrivateValue(Button.class, this.changeButton, warn, "onTooltip");
                ObfuscationReflectionHelper.setPrivateValue(Button.class, this.resetButton, warn, "onTooltip");
            }
        }

        @Inject(method="render", at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/components/Button;render(Lcom/mojang/blaze3d/vertex/PoseStack;IIF)V", ordinal=1))
        private void updateChangeActivity(CallbackInfo callback)
        {
            String keyName=this.key.getName();
            Minecraft mc=Minecraft.getInstance();
            this.changeButton.active=!mc.options.keyDrop.getName().equals(keyName)&&!mc.options.keySwapOffhand.getName().equals(keyName)||(mc.player==null||mc.gameMode==null||!mc.gameMode.getPlayerMode().isSurvival()||!mc.player.hasEffect(ENGMobEffects.ANEMIA.get()));
        }

        @Redirect(method="render", at=@At(value="FIELD", target="Lnet/minecraft/client/gui/components/Button;active:Z", opcode=Opcodes.PUTFIELD))
        private void updateResetActivity(Button button, boolean value)
        {
            String keyName=this.key.getName();
            Minecraft mc=Minecraft.getInstance();
            button.active=mc.options.keyDrop.getName().equals(keyName)||mc.options.keySwapOffhand.getName().equals(keyName)?(mc.player!=null?(mc.gameMode!=null&&mc.gameMode.getPlayerMode().isSurvival()?value&&!mc.player.hasEffect(ENGMobEffects.ANEMIA.get()):value):value):value;
        }
    }
}