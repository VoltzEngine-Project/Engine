package com.builtbroken.mc.lib.energy;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class EnergyHandler
{
		public final String moduleName;
		public final String fullUnit;
		public final String unit;

		/**
		 * Multiply UE energy by this ratio to convert it to the forgien ratio.
		 */
		public double toForgienEnergy;

		/**
		 * Multiply the forgien energy by this ratio to convert it into UE energy.
		 */
		public double toUEEnergy;

		public EnergyHandler(String moduleName, String fullUnit, String unit, double ratio)
		{
			this.moduleName = moduleName;
			this.fullUnit = fullUnit;
			this.unit = unit;
			this.toForgienEnergy = 1.0 / ratio;
			this.toUEEnergy = ratio;
		}

		public abstract double receiveEnergy(Object handler, ForgeDirection direction, double energy, boolean doReceive);

		public abstract double extractEnergy(Object handler, ForgeDirection direction, double energy, boolean doExtract);

		/**
		 * Charges an item with the given energy
		 *
		 * @param itemStack - item stack that is the item
		 * @param joules    - input energy
		 * @param docharge  - do the action
		 * @return amount of energy accepted
		 */
		public abstract double chargeItem(ItemStack itemStack, double joules, boolean docharge);

		/**
		 * discharges an item with the given energy
		 *
		 * @param itemStack   - item stack that is the item
		 * @param joules      - input energy
		 * @param doDischarge - do the action
		 * @return amount of energy that was removed
		 */
		public abstract double dischargeItem(ItemStack itemStack, double joules, boolean doDischarge);

		public abstract boolean doIsHandler(Object obj, ForgeDirection dir);

		public abstract boolean doIsEnergyContainer(Object obj);

		public abstract boolean canConnect(Object obj, ForgeDirection direction, Object source);

		public abstract ItemStack getItemWithCharge(ItemStack itemStack, double energy);

        public abstract double getEnergy(Object obj, ForgeDirection direction);

		public abstract double getMaxEnergy(Object handler, ForgeDirection direction);

		public abstract double getEnergyItem(ItemStack is);

		public abstract double getMaxEnergyItem(ItemStack is);
	}