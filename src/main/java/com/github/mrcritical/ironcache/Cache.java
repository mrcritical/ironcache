/**
 * 
 */
package com.github.mrcritical.ironcache;

/**
 * @author pjarrell
 * 
 */
public class Cache {

	private final String id;

	private final String name;

	private final String projectId;

	public Cache() {
		this(null, null, null);
	}

	public Cache(final String id, final String projectId, final String name) {
		super();
		this.id = id;
		this.projectId = projectId;
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Cache other = (Cache) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (projectId == null) {
			if (other.projectId != null) {
				return false;
			}
		} else if (!projectId.equals(other.projectId)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
		return result;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public Cache id(final String id) {
		return new Cache(id, projectId, name);
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public Cache name(final String name) {
		return new Cache(id, projectId, name);
	}

	/**
	 * @param projectId
	 *            the projectId to set
	 */
	public Cache projectId(final String projectId) {
		return new Cache(id, projectId, name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cache [");
		if (id != null) {
			builder.append("id=");
			builder.append(id);
			builder.append(", ");
		}
		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
		}
		if (projectId != null) {
			builder.append("projectId=");
			builder.append(projectId);
		}
		builder.append("]");
		return builder.toString();
	}

}
