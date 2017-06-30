package com.builtbroken.mc.lib.render.fx;

import com.builtbroken.mc.imp.transform.vector.Pos;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ReportedException;
import net.minecraft.world.World;

import java.awt.*;
import java.util.List;

@SideOnly(Side.CLIENT)
public class FXSmoke extends EntitySmokeFX
{
    //Legacy call
    public FXSmoke(World par1World, Pos position, float red, float green, float blue, float scale, double distance)
    {
        this(par1World, position, new Pos(0, 0, 0), red, green, blue, scale, distance, true);
    }

    public FXSmoke(World par1World, Pos position, Pos vel, float red, float green, float blue, float scale, double distance, boolean addColorVarient)
    {
        super(par1World, position.x(), position.y(), position.z(), vel.x(), vel.y(), vel.z(), scale);
        this.renderDistanceWeight = distance;
        this.particleRed = red;
        this.particleBlue = blue;
        this.particleGreen = green;

        if (addColorVarient)
        {
            float colorVarient = (float) (Math.random() * 0.90000001192092896D);
            this.particleRed *= colorVarient;
            this.particleBlue *= colorVarient;
            this.particleGreen *= colorVarient;
        }
    }

    public FXSmoke(World par1World, Pos position, Pos vel, Color color, float scale, double distance, boolean addColorVarient)
    {
        this(par1World, position, vel, color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255f, scale, distance, addColorVarient);
    }

    public FXSmoke setAge(int age)
    {
        this.particleMaxAge = age;
        return this;
    }

    @Override
    public void onUpdate()
    {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge)
        {
            this.setDead();
        }

        this.setParticleTextureIndex(7 - this.particleAge * 8 / this.particleMaxAge);
        this.motionY += 0.004D;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.posY == this.prevPosY)
        {
            this.motionX *= 1.1D;
            this.motionZ *= 1.1D;
        }

        this.motionX *= 0.9599999785423279D;
        this.motionY *= 0.9599999785423279D;
        this.motionZ *= 0.9599999785423279D;

        if (this.onGround)
        {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }
    }

    @Override
    public void moveEntity(double xx, double yy, double zz)
    {
        if (this.noClip)
        {
            this.boundingBox.offset(xx, yy, zz);
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
        }
        else
        {
            this.worldObj.theProfiler.startSection("move");
            this.ySize *= 0.4F;

            if (this.isInWeb)
            {
                this.isInWeb = false;
                xx *= 0.25D;
                yy *= 0.05000000074505806D;
                zz *= 0.25D;
                this.motionX = 0.0D;
                this.motionY = 0.0D;
                this.motionZ = 0.0D;
            }

            double d6 = xx;
            double d7 = yy;
            double d8 = zz;

            List list = this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox.addCoord(xx, yy, zz));
            if (list != null && list.size() > 0)
            {
                for (int i = 0; i < list.size(); ++i)
                {
                    if (list.get(i) != null)
                    {
                        yy = ((AxisAlignedBB) list.get(i)).calculateYOffset(this.boundingBox, yy);
                    }
                }
            }

            this.boundingBox.offset(0.0D, yy, 0.0D);

            if (!this.field_70135_K && d7 != yy)
            {
                zz = 0.0D;
                yy = 0.0D;
                xx = 0.0D;
            }

            if (list != null && list.size() > 0)
            {
                for (int j = 0; j < list.size(); ++j)
                {
                    if (list.get(j) != null)
                    {
                        xx = ((AxisAlignedBB) list.get(j)).calculateXOffset(this.boundingBox, xx);
                    }
                }
            }

            this.boundingBox.offset(xx, 0.0D, 0.0D);

            if (!this.field_70135_K && d6 != xx)
            {
                zz = 0.0D;
                yy = 0.0D;
                xx = 0.0D;
            }

            if (list != null && list.size() > 0)
            {
                for (int j = 0; j < list.size(); ++j)
                {
                    if (list.get(j) != null)
                    {
                        zz = ((AxisAlignedBB) list.get(j)).calculateZOffset(this.boundingBox, zz);
                    }
                }
            }

            this.boundingBox.offset(0.0D, 0.0D, zz);

            if (!this.field_70135_K && d8 != zz)
            {
                zz = 0.0D;
                yy = 0.0D;
                xx = 0.0D;
            }

            this.worldObj.theProfiler.endSection();
            this.worldObj.theProfiler.startSection("rest");
            this.posX = (this.boundingBox.minX + this.boundingBox.maxX) / 2.0D;
            this.posY = this.boundingBox.minY + (double) this.yOffset - (double) this.ySize;
            this.posZ = (this.boundingBox.minZ + this.boundingBox.maxZ) / 2.0D;
            this.isCollidedHorizontally = d6 != xx || d8 != zz;
            this.isCollidedVertically = d7 != yy;
            this.onGround = d7 != yy && d7 < 0.0D;
            this.isCollided = this.isCollidedHorizontally || this.isCollidedVertically;
            this.updateFallState(yy, this.onGround);

            if (d6 != xx)
            {
                this.motionX = 0.0D;
            }

            if (d7 != yy)
            {
                this.motionY = 0.0D;
            }

            if (d8 != zz)
            {
                this.motionZ = 0.0D;
            }

            try
            {
                this.func_145775_I();
            }
            catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
                this.addEntityCrashInfo(crashreportcategory);
                throw new ReportedException(crashreport);
            }

            this.worldObj.theProfiler.endSection();
        }
    }
}
