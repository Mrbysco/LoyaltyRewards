package com.mrbysco.loyaltyrewards.toasts;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.toasts.GuiToast;
import net.minecraft.client.gui.toasts.IToast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;

public class ToastReward implements IToast{
    public ItemStack stack;
    public String text;
	private long firstDrawTime;
    private boolean hasPlayedSound = false;

	public ToastReward(ItemStack stack, String text)
	{
		this.stack = stack == ItemStack.EMPTY ? new ItemStack(Blocks.COMMAND_BLOCK) : stack;
		this.text = text;
	}

	public IToast.Visibility draw(GuiToast toastGui, long delta)
	{
		if (this.firstDrawTime == 0)
			this.firstDrawTime = delta;

        toastGui.getMinecraft().getTextureManager().bindTexture(TEXTURE_TOASTS);
        GlStateManager.color(1.0F, 1.0F, 1.0F);
        toastGui.drawTexturedModalRect(0, 0, 0, 0, 160, 32);
        
        int i = 16746751;

        toastGui.getMinecraft().fontRenderer.drawString(this.stack.getDisplayName(), 30, 7, i | -16777216);
        toastGui.getMinecraft().fontRenderer.drawString(text, 30, 18, 0xffffff);
        
        if (!this.hasPlayedSound && delta > 0L)
        {
            this.hasPlayedSound = true;

            toastGui.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getRecord(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 1.0F, 1.0F));
        }
        
        RenderHelper.enableGUIStandardItemLighting();
        toastGui.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI((EntityLivingBase)null, this.stack, 8, 8);
        
        return delta >= 5000L ? IToast.Visibility.HIDE : IToast.Visibility.SHOW;

	}
}
