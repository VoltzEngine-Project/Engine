package resonant.lib.access;

import resonant.lib.type.TreeNode;

import java.util.List;

/**
 * @author Calclavia
 */
public class Permission extends TreeNode<Permission>
{
	public final String id;

	public Permission(String id)
	{
		this.id = id;
	}

	public Permission addChild(String perm)
	{
		return super.addChild(new Permission(perm));
	}

	public Permission find(String id)
	{
		for (Permission child : children)
		{
			if (child.id.equals(id))
			{
				return child;
			}
		}
		
		return null;
	}

	@Override
	public boolean equals(Object o)
	{
		return o instanceof Permission && ((Permission) o).id == id;
	}

	@Override
	public String toString()
	{
		List<Permission> list = getHierarchy();

		StringBuilder builder = new StringBuilder();

		for (Permission perm : list)
		{
			builder.append(perm.id);
			builder.append(".");
		}

		String name = builder.toString();

		return name.substring(0, name.length() - 1);
	}
}
