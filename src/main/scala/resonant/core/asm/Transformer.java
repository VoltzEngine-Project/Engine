package resonant.core.asm;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DRETURN;
import static org.objectweb.asm.Opcodes.FRETURN;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.IRETURN;
import static org.objectweb.asm.Opcodes.LRETURN;
import static org.objectweb.asm.Opcodes.RET;
import static org.objectweb.asm.Opcodes.RETURN;
import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import universalelectricity.core.asm.ASMHelper;
import universalelectricity.core.asm.ObfMapping;

/** @author Calclavia */
public class Transformer implements IClassTransformer
{
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        if (transformedName.equals("net.minecraft.world.chunk.Chunk"))
        {
            System.out.println("[Resonant-Engine] Transforming Chunk class for chunkModified event.");

            ClassNode cnode = ASMHelper.createClassNode(bytes);

            for (MethodNode method : cnode.methods)
            {
                ObfMapping m = new ObfMapping(cnode.name, method.name, method.desc).toRuntime();

                if (m.s_name.equals("setBlockIDWithMetadata") || m.s_name.equals("func_76592_a"))
                {
                    System.out.println("[Resonant-Engine] Found method " + m.s_name);
                    InsnList list = new InsnList();
                    list.add(new VarInsnNode(ALOAD, 0));
                    list.add(new VarInsnNode(ILOAD, 1));
                    list.add(new VarInsnNode(ILOAD, 2));
                    list.add(new VarInsnNode(ILOAD, 3));
                    list.add(new VarInsnNode(ILOAD, 4));
                    list.add(new VarInsnNode(ILOAD, 5));
                    list.add(new MethodInsnNode(INVOKESTATIC, "resonant/core/asm/StaticForwarder", "chunkSetBlockEvent", "(Lnet/minecraft/world/chunk/Chunk;IIIII)V"));

                    AbstractInsnNode lastInsn = method.instructions.getLast();
                    while (lastInsn instanceof LabelNode || lastInsn instanceof LineNumberNode)
                        lastInsn = lastInsn.getPrevious();

                    if (isReturn(lastInsn))
                        method.instructions.insertBefore(lastInsn, list);
                    else
                        method.instructions.insert(list);

                    System.out.println("[Resonant-Engine] Injected instruction to method: " + m.s_name);
                }
            }

            return ASMHelper.createBytes(cnode, 0);
        }

        return bytes;
    }

    private boolean isReturn(AbstractInsnNode node)
    {
        switch (node.getOpcode())
        {
            case RET:
            case RETURN:
            case ARETURN:
            case DRETURN:
            case FRETURN:
            case IRETURN:
            case LRETURN:
                return true;

            default:
                return false;
        }
    }

}
